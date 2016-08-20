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