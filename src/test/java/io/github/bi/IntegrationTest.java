package io.github.bi;

import io.github.bi.config.AsyncSyncConfiguration;
import io.github.bi.config.EmbeddedElasticsearch;
import io.github.bi.config.EmbeddedKafka;
import io.github.bi.config.EmbeddedSQL;
import io.github.bi.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MoneyMarketBiApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
