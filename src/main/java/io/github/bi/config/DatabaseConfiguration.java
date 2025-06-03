package io.github.bi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "io.github.bi.repository" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("io.github.bi.repository.search")
public class DatabaseConfiguration {}
