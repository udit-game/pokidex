package com.pokemon.pokidex.Config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class HttpClientConfig {

    @Bean(destroyMethod = "close")
    public CloseableHttpClient apacheHttpClient(PokedexProperties props) {
        PokedexProperties.Upstream up = props.upstream();

        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(up.maxTotalConnections())
                .setMaxConnPerRoute(up.maxPerRoute())
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.of(up.connectTimeout()))
                        .setSocketTimeout(Timeout.of(up.readTimeout()))
                        .build())
                .build();

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setUserAgent("pokidex/0.0.1")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setResponseTimeout(Timeout.of(up.readTimeout()))
                        .build())
                // retries IO errors and 429/503 only, never 404
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(up.maxRetries(), TimeValue.ofMilliseconds(300)))
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();
    }

    @Bean
    public RestTemplate pokeApiRestTemplate(CloseableHttpClient httpClient, PokedexProperties props) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        // base URL + proper encoding of {placeholders}
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(props.upstream().baseUrl()));
        return restTemplate;
    }
}