package io.advantageous.discovery.mesos;

import io.advantageous.discovery.DiscoveryService;
import io.advantageous.discovery.DiscoveryServiceFactory;

import java.net.URI;
import java.util.List;

public class MarathonDiscoveryServiceFactory implements DiscoveryServiceFactory {

    @Override
    public String getScheme() {
        return MarathonDiscoveryService.SCHEME;
    }

    @Override
    public DiscoveryService create(final List<URI> uris) {
        if (uris == null || uris.size() == 0)
            throw new IllegalArgumentException("you must specify a marathon configuration URI");
        if (uris.size() > 1)
            throw new UnsupportedOperationException("marathon service discovery only accepts one configuration URI");
        return new MarathonDiscoveryService(uris.get(0));
    }
}
