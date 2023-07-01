package com.bertflix.apigateway.middleware;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    public AuthorizationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            OkHttpClient client = new OkHttpClient();
            String authorization = Objects.requireNonNull(exchange.getRequest().getHeaders().get("Authorization")).get(0);

            Request request = new Request.Builder()
                    .url("http://localhost:8082/api/auth/validate")
                    .get()
                    .addHeader("Authorization", authorization)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(response.isSuccessful()){
                return chain.filter(exchange);
            }{
                throw new IllegalStateException("Not Authorized");
            }
        };
    }

    public static class Config {
        // contructors, getters and setters...
    }
}