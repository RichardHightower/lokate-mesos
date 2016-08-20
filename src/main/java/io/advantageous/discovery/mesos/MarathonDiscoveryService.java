package io.advantageous.discovery.mesos;

import io.advantageous.discovery.DiscoveryService;
import io.advantageous.reakt.promise.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static io.advantageous.reakt.promise.Promises.invokablePromise;

public class MarathonDiscoveryService implements DiscoveryService {


    static final String SCHEME = "marathon";

    private final Vertx vertx;
    private final int marathonPort;
    private final String marathonHost;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    MarathonDiscoveryService(final URI config) {
        Objects.requireNonNull(config, "you must specify a configuration URI for the mesos discovery service");
        if (!SCHEME.equals(config.getScheme()))
            throw new IllegalArgumentException("scheme for mesos service config must be " + SCHEME);
        this.vertx = Vertx.vertx();
        final URI configURI = URI.create(config.getSchemeSpecificPart());
        this.marathonPort = configURI.getPort();
        this.marathonHost = configURI.getHost();

        logger.info("Marathon discovery configured to {} {}", marathonHost, marathonPort);
    }


    static Map<String, String> splitQuery(String query) {
        if (query == null) return Collections.emptyMap();
        final Map<String, String> queryPairs = new LinkedHashMap<>();
        final String[] pairs = query.split("&|;");
        for (final String pair : pairs) {
            final int idx = pair.indexOf('=');
            queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return queryPairs;
    }

    @Override
    public Promise<List<URI>> lookupService(final URI queryURI) {
        return invokablePromise(promise -> {


            final int queryPort = queryURI.getPort() != -1 ? queryURI.getPort() : this.marathonPort;
            final String queryHost = queryURI.getHost() != null ? queryURI.getHost() : this.marathonHost;
            final String requestPath = "/v2/apps" + queryURI.getPath();

            if (logger.isDebugEnabled())
                logger.debug("Marathon discovery looking up service at {} {} {} {}", marathonHost, marathonPort,
                        requestPath, queryURI);

            this.vertx.createHttpClient()
                    .request(HttpMethod.GET, queryPort, queryHost, requestPath)
                    .exceptionHandler(promise::reject)
                    .handler(httpClientResponse -> httpClientResponse
                            .exceptionHandler(promise::reject)
                            .bodyHandler(buffer -> {
                                if (httpClientResponse.statusCode() == 200) {
                                    doLookupService(promise, buffer, queryURI);
                                } else {
                                    try {
                                        final String message = buffer.toJsonObject().getString("message");
                                        if (message.equals(String.format("App '%s' does not exist", queryURI.getPath()))) {
                                            promise.resolve(Collections.emptyList());
                                        } else {
                                            promise.reject(message);
                                        }
                                    } catch (Exception ex) {
                                        promise.reject("Server rejected request status code "
                                                + httpClientResponse.statusCode());
                                    }
                                }
                            }))
                    .end();
        });
    }


    protected void doLookupService(final Promise<List<URI>> promise,
                                   final Buffer buffer,
                                   final URI queryURI) {
        try {

            if (validateURI(queryURI, promise)) return;
            final Map<String, String> queryMap = splitQuery(queryURI.getQuery());

            final JsonObject app = buffer.toJsonObject().getJsonObject("app");
            final JsonArray tasks = app.getJsonArray("tasks");

            if (queryMap.containsKey("portIndex")) {
                final int portIndex = Integer.parseInt(queryMap.get("portIndex"));
                promise.resolve(extractURIs(tasks, portIndex));
            } else if (queryMap.containsKey("portName")) {
                int portIndex = findPortIndexByName(queryMap, app.getJsonArray("portDefinitions"));
                promise.resolve(extractURIs(tasks, portIndex));
            } else if (queryMap.containsKey("containerPort")) {
                final int containerPort = Integer.parseInt(queryMap.get("containerPort"));
                if (!app.containsKey("container")) {
                    promise.reject("Query with containerPort but Marathon has no container");
                    return;
                }
                int portIndex = findPortIndexByContainerPort(
                        app.getJsonObject("container").getJsonObject("docker").getJsonArray("portMappings"),
                        containerPort);
                promise.resolve(extractURIs(tasks, portIndex));

            } else {
                promise.reject("Did not understand the query params " + queryMap);
            }
        } catch (Exception ex) {
            logger.error("Unable to handle response from server {} {} response = {}", queryURI, ex.getMessage(), buffer.toString());
            logger.error("Exception from not being able to handle response from server ", ex);
            promise.reject("Unable to handle response from server " + queryURI, ex);
        }

    }

    private int findPortIndexByContainerPort(JsonArray portMappings, int containerPort) {
        int portIndex;
        for (portIndex = 0; portIndex < portMappings.size(); portIndex++) {
            final JsonObject portMapping = portMappings.getJsonObject(portIndex);
            if (portMapping.getInteger("containerPort") == containerPort) break;
        }
        return portIndex;
    }

    private List<URI> extractURIs(JsonArray tasks, int portIndex) {
        return tasks.stream()
                .map(task -> (JsonObject) task)
                .filter(task -> {
                    final JsonArray healthCheckResults = task.getJsonArray("healthCheckResults");
                    if (healthCheckResults.size() == 0) {
                        return true;
                    } else {
                        return healthCheckResults.stream().map(o -> (JsonObject) o)
                                .allMatch(health -> {
                                    if (health.getBoolean("alive") == null) return false;
                                    return health.getBoolean("alive");
                                });
                    }
                })
                .map(task -> URI.create(RESULT_SCHEME + "://" + task.getString("host") + ":"
                        + task.getJsonArray("ports").getInteger(portIndex))
                ).collect(Collectors.toList());
    }

    private int findPortIndexByName(Map<String, String> queryMap, JsonArray portDefinitions) {
        int portIndex;
        for (portIndex = 0; portIndex < portDefinitions.size(); portIndex++) {
            if (portDefinitions.getJsonObject(portIndex).containsKey("name") &&
                    portDefinitions.getJsonObject(portIndex).getString("name").equals(queryMap.get("portName"))) {
                break;
            }
        }
        return portIndex;
    }

    private boolean validateURI(URI queryURI, Promise<List<URI>> promise) {
        if (queryURI == null) {
            promise.reject("query was null");
            return true;
        }
        if (!SCHEME.equals(queryURI.getScheme())) {
            promise.reject(new IllegalArgumentException("query did not have the scheme " + SCHEME));
            return true;
        }
        return false;
    }


}
