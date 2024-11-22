package com.dosol.qwer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer를 구현하여 정적 리소스 경로를 추가로 설정하는 클래스입니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 정적 리소스 핸들러를 추가하여 외부 경로에 있는 파일을 정적 리소스로 제공할 수 있도록 설정합니다.
     *
     * @param registry ResourceHandlerRegistry 객체로 리소스 핸들러를 등록합니다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**") // 클라이언트 요청 URL 패턴
                /**
                 * 클라이언트가 "/upload/**" 경로로 요청하면,
                 * 실제 파일은 "C:/upload/" 디렉토리에서 매핑됩니다.
                 */
                .addResourceLocations("file:///C:/upload/");
        /**
         * "file:///"는 로컬 파일 시스템 경로를 가리킵니다.
         * 예: "/upload/profile_images/example.jpg" 요청 시
         * 실제 파일은 "C:/upload/profile_images/example.jpg"에서 검색됩니다.
         */
    }
}
