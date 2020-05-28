package com.example.demo;

import java.io.File;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.example.demo.properties.reloading.configs.ReloadableProperties;

@SpringBootApplication
public class SpringBootReloadablePropertiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReloadablePropertiesApplication.class, args);
	}
	
	@Bean
    @ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
    public PropertiesConfiguration propertiesConfiguration(
      @Value("${spring.config.location}") String path,
      @Value("${spring.properties.refreshDelay}") long refreshDelay) throws Exception {
		System.out.println("Inside PropertiesConfiguration propertiesConfiguration of main file");
        String filePath = path.substring("file:".length());
        PropertiesConfiguration configuration = new PropertiesConfiguration(new File(filePath).getCanonicalPath());
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(refreshDelay);
        configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        return configuration;
    }

    @Bean
    @ConditionalOnBean(PropertiesConfiguration.class)
    @Primary
    public Properties properties(PropertiesConfiguration propertiesConfiguration) throws Exception {
        ReloadableProperties properties = new ReloadableProperties(propertiesConfiguration);
        return properties;
    }

}
