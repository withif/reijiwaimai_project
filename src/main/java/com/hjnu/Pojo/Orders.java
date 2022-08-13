package com.hjnu.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
    private Integer status;


    //下单用户id
    @TableField("user_id")
    private Long userId;

    //地址id
    @TableField("address_book_id")
    private Long addressBookId;


    //下单时间
    @TableField("order_time")
    private LocalDateTime orderTime;


    //结账时间
    @TableField("checkout_time")
    private LocalDateTime checkoutTime;


    //支付方式 1微信，2支付宝
    @TableField("pay_method")
    private Integer payMethod;


    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    //用户名
    @TableField("username")
    private String userName;

    //手机号
    private String phone;

    //地址
    private String address;

    //收货人
    private String consignee;
}
