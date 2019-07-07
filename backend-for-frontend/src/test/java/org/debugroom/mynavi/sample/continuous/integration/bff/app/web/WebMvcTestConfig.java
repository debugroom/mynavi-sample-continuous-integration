package org.debugroom.mynavi.sample.continuous.integration.bff.app.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * WebMvcTest needs @SpringBootConfiguration class in parent hierarchy
 * See https://stackoverflow.com/questions/39084491/unable-to-find-a-springbootconfiguration-when-doing-a-jpatest
 */
@SpringBootApplication
public class WebMvcTestConfig {
}
