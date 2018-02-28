package com.getset.webfluxdemo.config;

import com.getset.webfluxdemo.handler.TimeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
//    @Autowired
//    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return route(GET("/time"), TimeHandler::getTime)
                .andRoute(GET("/date"), TimeHandler::getDate)
                .andRoute(GET("/times"), TimeHandler::sendTimePerSec);
    }
}
