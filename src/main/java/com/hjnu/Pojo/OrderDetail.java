package com.hjnu.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //订单id
    @TableField("order_id")
    private Long orderId;


    //菜品id
    @TableField("dish_id")
    private Long dishId;


    //套餐id
    @TableField("setmeal_id")
    private Long setmealId;


    //口味
    @TableField("dish_flavor")
    private String dishFlavor;


    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;
}
