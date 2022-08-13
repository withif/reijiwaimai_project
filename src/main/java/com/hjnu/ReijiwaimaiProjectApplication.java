package com.hjnu;

import lombok.extern.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.*;

/**
 * 静态资源放在static目录下，即可直接访问：localhost/backend/index.html
 * 如果不放在static目录下，则可以配置mvc框架的静态资源映射
 */
@Slf4j
@EnableTransactionManagement
@SpringBootApplication
public class ReijiwaimaiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReijiwaimaiProjectApplication.class, args);
        log.info("jetty服务器启动成功");
    }

}
