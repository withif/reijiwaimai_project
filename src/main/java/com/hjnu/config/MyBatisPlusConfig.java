package com.hjnu.config;

import com.baomidou.mybatisplus.extension.plugins.*;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import org.springframework.context.annotation.*;

/**
 * 创建mybatisplus分页插件
 */
@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //创建MybatisPlusInterceptor
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();
        //创建分页拦截器PaginationInnerInterceptor
        PaginationInnerInterceptor paginationInnerInterceptor=new PaginationInnerInterceptor();
        //将分页拦截器加入到MybatisPlusInterceptor
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }
}
