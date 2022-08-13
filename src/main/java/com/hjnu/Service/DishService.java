package com.hjnu.Service;

import com.baomidou.mybatisplus.extension.service.*;
import com.hjnu.DTO.*;
import com.hjnu.Pojo.*;
import org.springframework.stereotype.*;

@Service
public interface DishService extends IService<Dish> {


    //新增菜品，同时插入菜品对应的口味数据，需要对两张表操作
    public void addDishWithFlaver(DishDto dishDto);

    //根据id查询菜品信息和相对应的口味数据
    public DishDto getDishWithFlaver(Long id);

    //根据前端返回来的信息进行更新，执行了两张表
    public void updateDishWithFlavor(DishDto dishDto);

    public void deletebyid(Long ids);
}
