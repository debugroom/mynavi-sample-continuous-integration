package org.debugroom.mynavi.sample.continuous.integration.bff.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.CommonExceptionHandler;

public class TestConfig {

    @Import(TestMvcConfig.class)
    @Configuration
    public static class UnitTestConfig{
        @Bean
        public MessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource(){
                @Override
                public void setResourceLoader(ResourceLoader resourceLoader) {
                }
            };
            messageSource.setBasename("messages");
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }
    }

    @Configuration
    public static class TestMvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/");
        }
        @Bean("commonExceptionHandlerForUnitTest")
        CommonExceptionHandler commonExceptionHandler(){
            return new CommonExceptionHandler();
        }
    }

    @SpringBootApplication
    public static class EndToEndTestConfig{
    }

}
