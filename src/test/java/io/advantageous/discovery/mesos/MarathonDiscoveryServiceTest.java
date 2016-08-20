package io.advantageous.discovery.mesos;

import io.advantageous.discovery.DiscoveryService;
import io.advantageous.reakt.promise.Promise;
import io.advantageous.reakt.promise.Promises;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MarathonDiscoveryServiceTest {
    final MarathonDiscoveryService marathonDiscoveryService = new MarathonDiscoveryService(URI.create("marathon://localhost:8600/"));
    final JsonObject jsonObject = new JsonObject("{\n" +
            "  \"app\": {\n" +
            "    \"id\": \"/sample-dcos3\",\n" +
            "    \"cmd\": \"/opt/install.sh\",\n" +
            "    \"args\": null,\n" +
            "    \"user\": null,\n" +
            "    \"env\": {\n" +
            "      \"LOGSTASH_HOST\": \"logstash.marathon.mesos\",\n" +
            "      \"JAVA_ZIP_LOCATION\": \"https://s3-us-west-2.amazonaws.com/sample-deploy/sample-dcos-cas-0.9.5.zip\",\n" +
            "      \"JAVA_RUN_DEBUG\": \"TRUE\",\n" +
            "      \"LOGSTASH_PORT\": \"10514\",\n" +
            "      \"DEPLOYMENT_ENVIRONMENT\": \"integration\",\n" +
            "      \"LOGBACK_PATTERN\": \"%d{HH:mm:ss.SSS}-[%-5level] %logger{15} - %msg%n\",\n" +
            "      \"JAVA_RUN_COMMAND\": \"/opt/sample-dcos-cas-0.9.5/bin/sample-dcos-cas\"\n" +
            "    },\n" +
            "    \"instances\": 3,\n" +
            "    \"cpus\": 0.5,\n" +
            "    \"mem\": 1024,\n" +
            "    \"disk\": 0,\n" +
            "    \"executor\": \"\",\n" +
            "    \"constraints\": [],\n" +
            "    \"uris\": [],\n" +
            "    \"fetch\": [],\n" +
            "    \"storeUrls\": [],\n" +
            "    \"ports\": [\n" +
            "      10101,\n" +
            "      10102\n" +
            "    ],\n" +
            "    \"portDefinitions\": [\n" +
            "      {\n" +
            "        \"port\": 10101,\n" +
            "        \"protocol\": \"tcp\",\n" +
            "        \"name\": \"eventbus\",\n" +
            "        \"labels\": {}\n" +
            "      },\n" +
            "      {\n" +
            "        \"port\": 10102,\n" +
            "        \"protocol\": \"tcp\",\n" +
            "        \"name\": \"admin\",\n" +
            "        \"labels\": {}\n" +
            "      }\n" +
            "    ],\n" +
            "    \"requirePorts\": false,\n" +
            "    \"backoffSeconds\": 1,\n" +
            "    \"backoffFactor\": 1.15,\n" +
            "    \"maxLaunchDelaySeconds\": 3600,\n" +
            "    \"container\": {\n" +
            "      \"type\": \"DOCKER\",\n" +
            "      \"volumes\": [],\n" +
            "      \"docker\": {\n" +
            "        \"image\": \"advantageous/run-java-zip:0.2\",\n" +
            "        \"network\": \"BRIDGE\",\n" +
            "        \"portMappings\": [\n" +
            "          {\n" +
            "            \"containerPort\": 8081,\n" +
            "            \"hostPort\": 0,\n" +
            "            \"servicePort\": 10101,\n" +
            "            \"protocol\": \"tcp\",\n" +
            "            \"labels\": {}\n" +
            "          },\n" +
            "          {\n" +
            "            \"containerPort\": 9090,\n" +
            "            \"hostPort\": 0,\n" +
            "            \"servicePort\": 10102,\n" +
            "            \"protocol\": \"tcp\",\n" +
            "            \"labels\": {}\n" +
            "          }\n" +
            "        ],\n" +
            "        \"privileged\": false,\n" +
            "        \"parameters\": [],\n" +
            "        \"forcePullImage\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"healthChecks\": [\n" +
            "      {\n" +
            "        \"path\": \"/__admin/ok\",\n" +
            "        \"protocol\": \"HTTP\",\n" +
            "        \"portIndex\": 1,\n" +
            "        \"gracePeriodSeconds\": 30,\n" +
            "        \"intervalSeconds\": 20,\n" +
            "        \"timeoutSeconds\": 20,\n" +
            "        \"maxConsecutiveFailures\": 3,\n" +
            "        \"ignoreHttp1xx\": false\n" +
            "      }\n" +
            "    ],\n" +
            "    \"readinessChecks\": [],\n" +
            "    \"dependencies\": [],\n" +
            "    \"upgradeStrategy\": {\n" +
            "      \"minimumHealthCapacity\": 0,\n" +
            "      \"maximumOverCapacity\": 1\n" +
            "    },\n" +
            "    \"labels\": {\n" +
            "      \"HAPROXY_GROUP\": \"external\"\n" +
            "    },\n" +
            "    \"acceptedResourceRoles\": null,\n" +
            "    \"ipAddress\": null,\n" +
            "    \"version\": \"2016-08-19T20:03:20.797Z\",\n" +
            "    \"residency\": null,\n" +
            "    \"versionInfo\": {\n" +
            "      \"lastScalingAt\": \"2016-08-19T20:03:20.797Z\",\n" +
            "      \"lastConfigChangeAt\": \"2016-08-19T20:03:20.797Z\"\n" +
            "    },\n" +
            "    \"tasksStaged\": 0,\n" +
            "    \"tasksRunning\": 3,\n" +
            "    \"tasksHealthy\": 3,\n" +
            "    \"tasksUnhealthy\": 0,\n" +
            "    \"deployments\": [],\n" +
            "    \"tasks\": [\n" +
            "      {\n" +
            "        \"id\": \"sample-dcos3.fa3d20ff-6647-11e6-8de6-ea046373ee46\",\n" +
            "        \"slaveId\": \"6a1f41e3-331f-4537-ac95-441184a22f5d-S2\",\n" +
            "        \"host\": \"10.16.204.94\",\n" +
            "        \"startedAt\": \"2016-08-19T20:03:21.687Z\",\n" +
            "        \"stagedAt\": \"2016-08-19T20:03:20.840Z\",\n" +
            "        \"ports\": [\n" +
            "          19771,\n" +
            "          19772\n" +
            "        ],\n" +
            "        \"version\": \"2016-08-19T20:03:20.797Z\",\n" +
            "        \"ipAddresses\": [\n" +
            "          {\n" +
            "            \"ipAddress\": \"172.17.1.201\",\n" +
            "            \"protocol\": \"IPv4\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"appId\": \"/sample-dcos3\",\n" +
            "        \"healthCheckResults\": [\n" +
            "          {\n" +
            "            \"alive\": true,\n" +
            "            \"consecutiveFailures\": 0,\n" +
            "            \"firstSuccess\": \"2016-08-19T20:03:41.056Z\",\n" +
            "            \"lastFailure\": null,\n" +
            "            \"lastSuccess\": \"2016-08-19T23:35:53.685Z\",\n" +
            "            \"lastFailureCause\": null,\n" +
            "            \"taskId\": \"sample-dcos3.fa3d20ff-6647-11e6-8de6-ea046373ee46\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"sample-dcos3.fa680191-6647-11e6-8de6-ea046373ee46\",\n" +
            "        \"slaveId\": \"6a1f41e3-331f-4537-ac95-441184a22f5d-S0\",\n" +
            "        \"host\": \"10.16.204.95\",\n" +
            "        \"startedAt\": \"2016-08-19T20:03:22.036Z\",\n" +
            "        \"stagedAt\": \"2016-08-19T20:03:21.122Z\",\n" +
            "        \"ports\": [\n" +
            "          14125,\n" +
            "          14126\n" +
            "        ],\n" +
            "        \"version\": \"2016-08-19T20:03:20.797Z\",\n" +
            "        \"ipAddresses\": [\n" +
            "          {\n" +
            "            \"ipAddress\": \"172.17.2.138\",\n" +
            "            \"protocol\": \"IPv4\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"appId\": \"/sample-dcos3\",\n" +
            "        \"healthCheckResults\": [\n" +
            "          {\n" +
            "            \"alive\": true,\n" +
            "            \"consecutiveFailures\": 0,\n" +
            "            \"firstSuccess\": \"2016-08-19T20:03:41.039Z\",\n" +
            "            \"lastFailure\": null,\n" +
            "            \"lastSuccess\": \"2016-08-19T23:35:53.722Z\",\n" +
            "            \"lastFailureCause\": null,\n" +
            "            \"taskId\": \"sample-dcos3.fa680191-6647-11e6-8de6-ea046373ee46\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"sample-dcos3.fa67da80-6647-11e6-8de6-ea046373ee46\",\n" +
            "        \"slaveId\": \"6a1f41e3-331f-4537-ac95-441184a22f5d-S0\",\n" +
            "        \"host\": \"10.16.204.95\",\n" +
            "        \"startedAt\": \"2016-08-19T20:03:22.040Z\",\n" +
            "        \"stagedAt\": \"2016-08-19T20:03:21.121Z\",\n" +
            "        \"ports\": [\n" +
            "          14929,\n" +
            "          14930\n" +
            "        ],\n" +
            "        \"version\": \"2016-08-19T20:03:20.797Z\",\n" +
            "        \"ipAddresses\": [\n" +
            "          {\n" +
            "            \"ipAddress\": \"172.17.2.139\",\n" +
            "            \"protocol\": \"IPv4\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"appId\": \"/sample-dcos3\",\n" +
            "        \"healthCheckResults\": [\n" +
            "          {\n" +
            "            \"alive\": true,\n" +
            "            \"consecutiveFailures\": 0,\n" +
            "            \"firstSuccess\": \"2016-08-19T20:03:41.004Z\",\n" +
            "            \"lastFailure\": null,\n" +
            "            \"lastSuccess\": \"2016-08-19T23:35:53.635Z\",\n" +
            "            \"lastFailureCause\": null,\n" +
            "            \"taskId\": \"sample-dcos3.fa67da80-6647-11e6-8de6-ea046373ee46\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}\n");


    @Test
    public void testCreate() throws Exception {

        final DiscoveryService discoveryService = DiscoveryService.create(URI.create("marathon://marathon.mesos:8080/"));

    }

    @Test
    public void doLookupServiceByPortIndex() throws Exception {

        final Promise<List<URI>> promise = Promises.promiseList(URI.class);

        marathonDiscoveryService.doLookupService(promise, jsonObject,
                URI.create("marathon:///sample-dcos3?portIndex=0"));
        validateURIs(promise);
    }

    @Test
    public void doLookupServiceByPortName() throws Exception {

        final Promise<List<URI>> promise = Promises.promiseList(URI.class);

        marathonDiscoveryService.doLookupService(promise, jsonObject,
                URI.create("marathon:///sample-dcos3?portName=eventbus"));
        validateURIs(promise);
    }

    @Test
    public void doLookupServiceByContainerPort() throws Exception {

        final Promise<List<URI>> promise = Promises.promiseList(URI.class);

        marathonDiscoveryService.doLookupService(promise, jsonObject,
                URI.create("marathon:///sample-dcos3?containerPort=8081"));
        validateURIs(promise);
    }

    @Test(expected = Exception.class)
    public void errorBadQueryParam() throws Exception {

        final Promise<List<URI>> promise = Promises.promiseList(URI.class);

        marathonDiscoveryService.doLookupService(promise, jsonObject,
                URI.create("marathon:///sample-dcos3?what=8081"));
        validateURIs(promise);
    }

    @Test(expected = Exception.class)
    public void errorBadUri() throws Exception {

        final Promise<List<URI>> promise = Promises.promiseList(URI.class);

        marathonDiscoveryService.doLookupService(promise, jsonObject,
                URI.create("what:///sample-dcos3?containerPort=8081"));
        validateURIs(promise);
    }

    private void validateURIs(Promise<List<URI>> promise) {
        final List<URI> uriList = promise.get();
        assertNotNull(uriList);
        assertEquals(3, uriList.size());
        assertEquals("10.16.204.94", uriList.get(0).getHost());
        assertEquals(19771, uriList.get(0).getPort());
    }

}