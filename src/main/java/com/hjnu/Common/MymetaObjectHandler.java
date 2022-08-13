package com.hjnu.Common;

import com.baomidou.mybatisplus.core.handlers.*;
import lombok.extern.slf4j.*;
import org.apache.ibatis.reflection.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.servlet.http.*;
import java.time.*;

/**
 *              自定义元数据处理器
 * 公共字段自动填充
 * 客户端每次发送http请求，对应的服务器都会分配一个新的线程来处理,
 * 可以使用session对象，但Filter和controller和MetaObjectHandler都处于同一个线程，还可以使用ThreadLocal
 * ThreadLocal并不是一个Thread，而是Thread的局部变量
 * ThreadLocal为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问当前线程对应的值。
 */
@Slf4j
@Component
public class MymetaObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpSession session;
    /**
     * 执行插入语句时自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("执行公共字段自动填充[INSERT]");
        log.info("当前线程的id为：{}",Thread.currentThread().getId());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //使用ThreadLocal获取(在过滤器中使用ThreadLocal存入的东西)
        metaObject.setValue("createUser",BaseContext.getUserId());
        metaObject.setValue("updateUser",BaseContext.getUserId());
//        //使用session获取id
//        metaObject.setValue("createUser",session.getAttribute("employee_id"));
//        metaObject.setValue("updateUser",session.getAttribute("employee_id"));
        log.info(metaObject.toString());
    }

    /**
     * 执行更新语句时自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("执行公共字段自动填充[UPDATE]");
        log.info("当前线程的id为：{}",Thread.currentThread().getId());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getUserId());
//        metaObject.setValue("updateUser",session.getAttribute("employee_id"));//只针对employee表
        log.info(metaObject.toString());
    }
}
