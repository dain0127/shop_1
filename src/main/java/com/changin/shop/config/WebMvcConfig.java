package com.changin.shop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // application.yml 설정 파일의, 프로퍼티를 코드내 변수로 가져올 수 있다.
    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 1. 이미지를 위한 커스텀 설정 (올바름)
        // 이미지 파일을 조회할 때, (url) /images/** 에 매핑시킨다.
        registry.addResourceHandler("/images/**")
                // 위 url이 매핑되었을 때, 실제 파일을 읽어올 위치.
                // file:/// 경로는 file: 접두사를 사용하여 명시하는 것이 일반적입니다.
                .addResourceLocations(uploadPath);


        // ⚠️ 2. 이전에 404를 유발했던 '모든 경로 오버라이드' 코드를 제거합니다.
        // registry.addResourceHandler("/**")
        //         .addResourceLocations(uploadPath); // 이 코드를 제거해야 합니다.


        // 3. 스프링 부트의 기본 정적 리소스 핸들러를 다시 추가합니다. (권장)
        // WebMvcConfigurer를 구현하면 기본 설정이 사라지므로,
        // /static/ 등 클래스패스 리소스를 명시적으로 추가하여 CSS/JS/HTML 등을 찾게 합니다.
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/");


        // 4. (선택적) WebMvcConfigurer.super.addResourceHandlers(registry);는 사실상 필요 없으므로 제거해도 무방합니다.
    }
}