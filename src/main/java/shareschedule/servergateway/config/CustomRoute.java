package shareschedule.servergateway.config;

import lombok.RequiredArgsConstructor;
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
                        .uri("lb://CALENDAR"))
                .route("dev", r -> r.path("/dev/**")
                        .uri("lb://USER"))
                // jwt 필터
                .route("userJwtFilter", r -> r.path("/users/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://USER"))
                .route("scheduleJwtFilter", r -> r.path("/schedules/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("lb://SCHEDULE"))
                .build();
    }
}
