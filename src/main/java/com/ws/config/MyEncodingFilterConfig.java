package com.ws.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 编码过滤器
 * @author WS-
 */
@Configuration
public class MyEncodingFilterConfig {
    @Bean
    public FilterRegistrationBean myCharaEncoding(){
        FilterRegistrationBean myFilter = new FilterRegistrationBean();
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setForceEncoding(true);
        myFilter.setFilter(encodingFilter);
        myFilter.addUrlPatterns("/*");
        return myFilter;
    }
}
