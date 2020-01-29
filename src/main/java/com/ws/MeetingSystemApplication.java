package com.ws;

import com.ws.common.utils.ShiroUtil;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.util.HashMap;

@SpringBootApplication
@MapperScan("com.ws.mapper")
public class MeetingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingSystemApplication.class, args);
    }

  /*  @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringbootdemoApplication.class);
    }*/

    @Bean(initMethod = "init", name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        try {
            // WebAppResourceLoader 配置root路径是关键
            WebAppResourceLoader webAppResourceLoader = new WebAppResourceLoader(patternResolver.getResource("classpath:/").getFile().getPath());
            beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
           /*使用jar包运行时使用以下方式加载模板*/
            /*ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader("classpath:/");
            beetlGroupUtilConfiguration.setResourceLoader(classpathResourceLoader);*/
            beetlGroupUtilConfiguration.init();
            HashMap<String, Object> functionPackages = new HashMap<>();
            functionPackages.put("shiro",new ShiroUtil());
            beetlGroupUtilConfiguration.setFunctionPackages(functionPackages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //读取配置文件信息
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setPrefix("/templates/");
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }

}
