package com.hjnu.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.extension.service.impl.*;
import com.hjnu.Common.*;
import com.hjnu.Mapper.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private     DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //判断是否关联菜品
        LambdaQueryWrapper<Dish> DishlambdaQueryWrapper=new LambdaQueryWrapper();
        DishlambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(DishlambdaQueryWrapper);//select count(*) from dish where CategoryId=id
        //如果关联菜品，则抛出一个业务异常
        if(count1>0){
            throw new CliException("当前分类已关联菜品，无法删除");
        }
        //判断是否关联套餐
        LambdaQueryWrapper<Setmeal> SetmeallambdaQueryWrapper=new LambdaQueryWrapper();
        SetmeallambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long count2 = setmealService.count(SetmeallambdaQueryWrapper);
        if(count2>0){
            throw new CliException("当前分类已关联套餐，无法删除");
        }
        //如果走到这，则说明都没有关联
        super.removeById(id);
    }
}
