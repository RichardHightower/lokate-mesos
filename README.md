# lokate-mesos
Uses Reakt Lokate Lib but add support for querying Mesos/Marathon location. 


## Search by container port
```
# Default host
discovery:marathon:///eventbus.sample-dcos3?containerPort=8081 
# Specify host
discovery:marathon://marathon.mesos:8080/eventbus.sample-dcos3?containerPort=8081
```

## Search by port name
```
# Default host
discovery:marathon:///eventbus.sample-dcos3?portName=eventbus
# Specify host
discovery:marathon://marathon.mesos:8080/eventbus.sample-dcos3?portName=eventbus
```


## Search by port index
```
# Default host
discovery:marathon:///eventbus.sample-dcos3?portIndex=0
# Specify host
discovery:marathon://marathon.mesos:8080/eventbus.sample-dcos3?portIndex=0
```


## Config URL
```java
URI.create("marathon://marathon.mesos:8080/")
```

See [Lokate](https://github.com/advantageous/lokate) for more sample usage. 


## Example

####Gradle file
```

    //Service discovery
    compile 'io.advantageous.discovery:lokate-mesos:1.1.5'
```


#### Create discovery service that talks to mesos
```java
@RequestMapping("/todo-service")
public class TodoServiceImpl implements TodoService {

    ...
    private final DiscoveryService discoveryService;
    

    public TodoServiceImpl(ServiceManagementBundle mgmt, TodoRepo todoRepo) {
         ...
        logger.info("Creating discovery service");
        discoveryService = DiscoveryService.create(URI.create("marathon://marathon.mesos:8080/"));


        logger.info("Todo service created");
    }
```


#### Use it
```java

...
    @POST(value = "/service")
    public final Promise<List<URI>> listServices(URI uri) {
        logger.debug("List services");
        return discoveryService.lookupService(uri);
    }
```


#### Hit it from REST

```sh
$  curl -X POST http://public-slave:10101/v1/todo-service/service   -d '"discovery:marathon:///sample-dcos3?portIndex=0"' -H "Content-type: application/json" | jq .
[
  "service://10.16.204.94:2838",
  "service://10.16.204.95:26078",
  "service://10.16.204.95:23568"
]

```

Lokate uses [Reakt](http://advantageous.github.io/reakt/) for its async promises.
