package com.hjnu.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.baomidou.mybatisplus.extension.service.impl.*;
import com.hjnu.Common.*;
import com.hjnu.DTO.*;
import com.hjnu.Mapper.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Transactional
    @Override
    public void addSetmealwithDish(SetmealDto setmealDto) {
        //1.保存套餐基本信息,setmeal表执行insert
        this.save(setmealDto);
        /**
         * 2.保存菜品和套餐的关联信息，对setmeal_Dish表执行insert语句
         * 前端传来的数据中缺少套餐id，即setmealId
         */
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());//将套餐id注入到list集合
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleyemealwithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus,1);
        long count = this.count(lambdaQueryWrapper);
        if(count>0){
            throw new CliException("该套餐正在售卖，无法删除");
        }
        //如果可以删除，则先删除setmeal表中的数据
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(dishLambdaQueryWrapper);
     }

    @Transactional
    @Override
    public void updateSetmealwithDish(SetmealDto setmealDto) {
        //更新setmeal
        this.updateById(setmealDto);
        //更新setmeal_dish,先删除再添加
        LambdaUpdateWrapper<SetmealDish> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(lambdaUpdateWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }


}
