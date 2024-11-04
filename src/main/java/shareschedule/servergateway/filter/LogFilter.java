package shareschedule.servergateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class LogFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        RequestPath path = request.getPath();
        List<String> userId = request.getHeaders().getOrEmpty("X-UserId");
        if(!userId.isEmpty()){
            infoRequest(userId.get(0), method, path);
        } else{
            infoRequest(request.getRemoteAddress().toString(), method, path);
        }
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    //TODO: 응답 직전 처리 필요 시 추가
                })
        );
    }

    private static void infoRequest(String request, HttpMethod method, RequestPath path) {
        log.info("[{}:{}] -> {}", request, method, path);
    }
}
