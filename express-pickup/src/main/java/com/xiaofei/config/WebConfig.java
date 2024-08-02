package com.xiaofei.config;

import com.xiaofei.filter.AllRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域问题
 *
 * @date 2022/3/31 21:39
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    public  HandlerInterceptor interceptor(){
        return new AllRequestInterceptor();
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
        patterns.add("/user/login");
        patterns.add("/user/register");
        patterns.add("/user/logout");
        patterns.add("/swagger-ui.html/**");
        patterns.add("/webjars/**");
        patterns.add("/v2/**");
        patterns.add("/swagger-resources/**");
        patterns.add("/doc.html/**");
        //注册拦截器类，添加黑名单(addPathPatterns("/**")),‘/*’只拦截一个层级，'/**'拦截全部
        // 和白名单(excludePathPatterns("List类型参数"))，将不必拦截的路径添加到List列表中
        registry.addInterceptor(interceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域相关配置, 并让 authorization 可在响应头中出现
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "PATCH", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        super.addResourceHandlers(registry);
    }
}
