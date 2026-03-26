package com.admin.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        System.out.println("=== 网关请求 ===");
        System.out.println("请求路径: " + path);

        // 跳过不需要认证的路径
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register") || path.startsWith("/files/")) {
            System.out.println("跳过认证路径: " + path);
            return chain.filter(exchange);
        }

        String token = getToken(request);
        System.out.println("Token: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));

        if (!StringUtils.isEmpty(token)) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(getSigningKey())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                Object userIdObj = claims.get("userId");
                String userId = userIdObj != null ? String.valueOf(userIdObj) : null;

                System.out.println("Token解析成功 - 用户: " + username + ", ID: " + userId);

                // 将用户信息添加到请求头
                ServerHttpRequest.Builder builder = request.mutate();
                builder.header("X-User-Id", userId);
                builder.header("X-Username", username);

                // 获取角色信息
                List<String> roles = claims.get("roles", List.class);
                if (roles != null && !roles.isEmpty()) {
                    builder.header("X-User-Roles", String.join(",", roles));
                }

                return chain.filter(exchange.mutate().request(builder.build()).build());
            } catch (Exception e) {
                System.out.println("Token解析失败: " + e.getMessage());
                e.printStackTrace();
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }

        System.out.println("未提供Token,拒绝访问");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private String getToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
