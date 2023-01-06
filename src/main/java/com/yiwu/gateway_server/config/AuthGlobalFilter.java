/*
 * Copyright (c) 2012, 2021, Wedon and/or its affiliates. All rights reserved.
 *
 */

package com.yiwu.gateway_server.config;

import com.yiwu.common.Resp;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Author fuzf
 * @Date 2023-01-05 18:06
 * @Description:
 */

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Resource
    private HandleException handleException;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean flag = false;
        String path = exchange.getRequest().getURI().getPath();
        for (String url : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(url, path)) {
                flag = true;
                break;
            }
        }
        // 白名单放行
        if (flag) {
            return chain.filter(exchange);
        }
        //todo 可以做验证信息处理
        String access_token = exchange.getRequest().getQueryParams().getFirst("access_token");

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst("token");
        String organ = headers.getFirst("organ");
        String actor = headers.getFirst("actor");
        String accessToken = headers.getFirst("access_token");
        String channel = headers.getFirst("channel");
        String origin = headers.getFirst("origin");

        if (true) {
            //通过
        } else {
            return handleException.writeError(exchange,
                    "Token was not recognised, token: ".concat(access_token));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
