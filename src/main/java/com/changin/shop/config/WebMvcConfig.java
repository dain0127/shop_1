package com.changin.shop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //application.yml 설정 파일의, 프로퍼티를 코드내 변수로 가져올 수 있다.
    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //사용자가 이미지 파일을 조회할 때, (url) /images/** 에 매핑시킨다.
        registry.addResourceHandler("/images/**")
                //위 url이 매핑되었을 때, 실제 파일을 읽어올 위치.
                .addResourceLocations(uploadPath);

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
