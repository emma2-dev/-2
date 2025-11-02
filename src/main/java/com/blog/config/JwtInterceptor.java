package com.blog.config;

import com.blog.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预检请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 获取请求路径
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        logger.info("拦截器收到请求 - URI: {}, ContextPath: {}, Method: {}", 
                   requestURI, contextPath, request.getMethod());
        
        // 错误页面和静态资源直接放行
        if (requestURI.equals("/error") || 
            requestURI.equals("/favicon.ico") ||
            requestURI.startsWith("/static/") ||
            requestURI.startsWith("/webjars/")) {
            return true;
        }
        
        // 注意：排除路径的配置在WebMvcConfig中的excludePathPatterns已处理
        // 这里只处理需要认证的请求
        
        // 获取Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证Token
        if (token == null || !jwtUtil.validateToken(token)) {
            logger.info("请求需要认证但Token无效: {}", requestURI);
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            return false;
        }
        
        // 从Token中获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        
        logger.info("JWT拦截器 - userId: {}, username: {}, role: {}", userId, username, role);
        
        // 将用户信息存入request
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        request.setAttribute("role", role);
        
        return true;
    }
}


