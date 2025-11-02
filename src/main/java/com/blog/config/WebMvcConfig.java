package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Value("${file.upload-path}")
    private String uploadPath;
    
    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/auth/login",
                        "/api/auth/register",
                        "/auth/register",
                        "/api/article/list",
                        "/article/list",
                        "/api/article/recommend",
                        "/article/recommend",
                        "/api/category/list",
                        "/category/list",
                        "/api/category/hot",
                        "/category/hot",
                        "/api/tag/list",
                        "/tag/list",
                        "/api/tag/hot",
                        "/tag/hot",
                        "/api/note/list",
                        "/note/list",
                        "/api/message/list",
                        "/message/list",
                        "/api/comment/target",
                        "/comment/target",
                        "/api/comment/list",
                        "/comment/list",
                        "/api/files/**",
                        "/files/**",
                        "/api/carousel/list",
                        "/carousel/list",
                        "/api/statistics",
                        "/statistics",
                        "/api/file/image/**",
                        "/file/image/**",
                        "/api/file/download/**",
                        "/file/download/**"
                );
    }
    
    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**", "/api/files/**")
                .addResourceLocations("file:" + uploadPath);
    }
}

