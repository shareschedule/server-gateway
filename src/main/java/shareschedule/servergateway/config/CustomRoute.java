package shareschedule.servergateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shareschedule.servergateway.filter.JwtFilter;

@Configuration
@RequiredArgsConstructor
public class CustomRoute {

    private final JwtFilter jwtFilter;

    @Bean
    public RouteLocator cRoute(RouteLocatorBuilder builder) {

        return builder.routes()
                // 필터 X
                .route("user", r -> r.path("/users/login/**")
                        .uri("lb://SSUSER"))

                // jwt 필터
                .route("user", r -> r.path("/users/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://SSUSER"))
                .route("schedule", r -> r.path("/schedules/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://SSSCHEDULE"))
                .build();
    }
}