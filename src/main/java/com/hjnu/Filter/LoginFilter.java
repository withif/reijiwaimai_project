package com.hjnu.Filter;

import com.alibaba.fastjson.JSON;
import com.hjnu.Common.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
@Slf4j
@Configuration
@WebFilter(urlPatterns = "/*",filterName ="loginFilter" )
public class LoginFilter implements Filter {
    HttpServletRequest request;
    HttpServletResponse response;
    //路径匹配器，支持通配符
    private static  final  AntPathMatcher antPathMatcher=new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("过滤器执行");

        log.info("当前线程的id为："+Thread.currentThread().getId());
        request=(HttpServletRequest) servletRequest;
        response=(HttpServletResponse) servletResponse;
        Long employee_id =(Long) request.getSession().getAttribute("employee_id");
        Long userid =(Long) request.getSession().getAttribute("user");
        //将session中存的employee_id封装在ThreadLocal中

        //定义不需要拦截的路径
        String[] url={
                "/employee/login",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",    //获得验证码
                "/user/login"   //手机登录
        };
        boolean check=isURI(url,request.getRequestURI());
        log.info("请求的地址为{}",request.getRequestURI());
        if(check){
            filterChain.doFilter(request,response);
            log.info("过滤器已放行");
            return ;
        }
        if(employee_id!=null){
            BaseContext.setUserId(employee_id);
            log.info("用户已登录，用户id：{}",employee_id);
            filterChain.doFilter(request,response);
            log.info("过滤器已放行");
            return ;
        }
        if(userid!=null){
            BaseContext.setUserId(userid);
            log.info("用户已登录，用户id：{}",userid);
            filterChain.doFilter(request,response);
            log.info("过滤器已放行");
            return ;
        }

        String notlogin = JSON.toJSONString(R.error("NOTLOGIN"));
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//        response.getWriter().print(R.error("NOTLOGIN"));
        return;


    }




    public boolean isURI(String[] uri ,String requestURI){
        for(String path:uri){
            boolean match = antPathMatcher.match(path, requestURI);
            if(match){
                return  true;
            }

        }
        return false;
    }
}
