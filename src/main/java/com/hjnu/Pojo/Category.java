package com.hjnu.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.*;

/**
 *
 */
@Data
@TableName("category")
public class Category {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型   1 菜品分类 2 套餐分类
     */
    private Integer type;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)//插入时自动填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//更新和插入时自动填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)//插入时自动填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)//更新和插入时自动填充字段
    private Long updateUser;

}
