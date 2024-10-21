package shareschedule.servergateway.filter;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpResponseException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import shareschedule.servergateway.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(JwtFilter.Config config) {
        return ((exchange, chain) -> {
            String accessToken = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");
            try {
                long userId = jwtUtil.getUserId(accessToken);
                ServerHttpRequest request = exchange.getRequest();
                request.mutate().header("X-UserId", String.valueOf(userId));

                return chain.filter(exchange);
            } catch (HttpResponseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static class Config {

    }
}
