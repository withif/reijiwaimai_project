package com.hjnu.Service;

import com.baomidou.mybatisplus.extension.service.*;
import com.hjnu.DTO.*;
import com.hjnu.Pojo.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public interface SetmealService extends IService<Setmeal> {
    /**
     *新增套餐，包括套餐和菜品的关联关系
     */
    public void addSetmealwithDish(SetmealDto setmealDto);

    public void deleyemealwithDish(List<Long> ids);

    public void updateSetmealwithDish(SetmealDto setmealDto);
}
