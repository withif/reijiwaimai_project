package com.hjnu.Common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取id
 *              **ThreadLocal常用方法：**
 * A. public void set(T value) : 设置当前线程的线程局部变量的值
 * B. public T get() : 返回当前线程所对应的线程局部变量的值
 * C. public void remove() : 删除当前线程所对应的线程局部变量的值
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setUserId(Long id){
        threadLocal.set(id);
    }
    public static Long getUserId(){
        return threadLocal.get();
    }
}
