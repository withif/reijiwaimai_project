package com.hjnu.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //用户id
    @TableField("user_id")
    private Long userId;

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
    @TableField("create_time")
    private LocalDateTime createTime;
}
