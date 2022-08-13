package com.hjnu.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.extension.service.impl.*;
import com.hjnu.DTO.*;
import com.hjnu.Mapper.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 添加dish和dishflavor，需要操作两张表
     * @param dishDto
     */
    @Transactional
    @Override
    public void addDishWithFlaver(DishDto dishDto) {
        //保存菜品信息到菜品表dish
        boolean save1 = this.save(dishDto);
        //将菜品口味保存到菜品口味表dish_flavor,可以通过dishDto获得菜品id,利用循环将菜品ID加到数据传输对象的菜品口味中（flavors）中
        Long id = dishDto.getId();//菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((flavor)->{
            flavor.setDishId(id);
            return flavor;
        }).collect(Collectors.toList());
        boolean save2 = dishFlavorService.saveBatch(flavors);
    }

    /**
     * 查询菜品信息和对应的口味信息，两张表
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlaver(Long id) {
        DishDto dishDto=new DishDto();
        //查询菜品的基本信息
        Dish dish = this.getById(id);
        //对象拷贝
//         dishDto=(DishDto) dish;
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品的口味信息dish_flavor表
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getId()!=null,DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }
    @Transactional
    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);
        //更新dish_flavor表（先删除掉原来的数据，再新增新的数据）
        Long id = dishDto.getId();
        LambdaUpdateWrapper<DishFlavor> lambdaUpdateWrapper=new LambdaUpdateWrapper();
        lambdaUpdateWrapper.eq(DishFlavor::getDishId,id);
        boolean b = dishFlavorService.remove(lambdaUpdateWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> l=flavors.stream().map((item)->{
           item.setDishId(dishDto.getId());
           return item;
        }).collect(Collectors.toList());
        boolean b1 = dishFlavorService.saveBatch(l);
        log.info("执行结果");
    }
    /**
     * 删除菜品和对应的菜品口味信息
     */
    @Transactional
    @Override
    public void deletebyid(Long ids){
        //删除dish中的数据
//        this.deletebyid(ids);
        this.removeById(ids);
        //删除dish_flavor中的数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,ids);
        dishFlavorService.remove(lambdaQueryWrapper);
    }
}
