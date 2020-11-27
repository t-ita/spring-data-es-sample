package com.myexample.springdataessample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.myexample.springdataessample.repository.elasticsearch")
public class ElasticsearchConfig {
}
