package com.xiaofei.expresspickup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger2 接口文档配置类
 *
 * @date 2022/2/25 21:10
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    /*
     * description: 配置了Swagger的Docket的Bean实例
     * @Param: []
     * @Return: springfox.documentation.spring.web.plugins.Docket
     **/
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 配置扫描接口
                .select()
                /*
                 *RequestHandlerSelectors,配置要扫描接口的方式
                 * 参数说明:
                 * basePackage:基于包扫描
                 * class:基于类扫描
                 * any():扫描全部
                 * none():全部都不扫描
                 * withMethodAnnotation:通过方法的注解扫描
                 * // withMethodAnnotation(GetMapping.class))
                 * withClassAnnotation:通过类的注解扫描
                 */
                .apis(RequestHandlerSelectors.basePackage("com.xiaofei.expresspickup.controller"))
                // .paths()过滤,不扫描哪些接口
                .paths(PathSelectors.any())
                .build();
    }

    /*
     * description: 配置Swagger信息
     * @Param: []
     * @Return: springfox.documentation.service.ApiInfo
     **/
    private ApiInfo apiInfo() {
        // 配置作者信息
        Contact contact = new Contact("折腾的小飞",
                "http://www.xiaofei.work",
                "tu_aofei@163.com");
        // 配置API文档标题
        return new ApiInfo("毕业设计-快递代取系统",
                // API文档描述
                "一个基于springboot + vue 的快递代取系统",
                // API版本号
                "1.0",
                // 配置URL(公司官网/blog地址)
                "https://www.xiaofei.work",
                // 作者信息
                contact,
                // 以下内容默认即可
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
