package org.debugroom.mynavi.sample.continuous.integration.bff.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="service")
public class ServiceProperties {

    private String dns;
    
}
