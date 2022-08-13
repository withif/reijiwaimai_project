package com.hjnu.config;

import com.hjnu.Common.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.*;
import org.springframework.web.servlet.config.annotation.*;

import java.util.*;


@Configuration
@Slf4j
public class mvcConfig extends WebMvcConfigurationSupport {
    /**
     * 配置mvc框架的静态资源映射
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(1);
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/static/front/");
        log.info("静态资源加载成功");
        //配置静态资源处理
//        registry.addResourceHandler("/**")
//                .addResourceLocations("resources/", "static/", "public/")
//                .addResourceLocations("classpath:resources/", "classpath:static/");
    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用jackson将Object转成Json对象
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        /**
         * 将消息转换器添加到mvc框架的转换器集合中
         * index设置为0，表示优先使用这个消息转换器
         */
        converters.add(0,messageConverter);
        log.info("消息转换器加载成功");
//        super.extendMessageConverters(converters);
    }

}
