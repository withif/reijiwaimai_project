package com.hjnu.Controller;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.hjnu.Common.*;
import com.hjnu.DTO.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.addSetmealwithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name){
        Page<SetmealDto> setmealDtoPage=new Page<>();
        Page<Setmeal> setmealPage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasLength(name),Setmeal::getName,name);//
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, lambdaQueryWrapper);
        //将setmealpage拷贝到setmealDtopage,不拷贝records,还需要修改里面的内容
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> l=    records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(l);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleyemealwithDish(ids);
        return R.success("套餐数据删除成功");
    }
    @PostMapping("/status/{status}")
    public R<String > changeStatus(@PathVariable Integer status,@RequestParam List<Long> ids){
        LambdaUpdateWrapper<Setmeal> lambdaQueryWrapper=new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.set(status!=null,Setmeal::getStatus,status);
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealService.update(lambdaQueryWrapper);
        return R.success("停售成功");
    }

    /**
     * http://localhost/setmeal/list?categoryId=1413386191767674881&status=1
     */
    @GetMapping("/list")
    public R<List> list(@RequestParam Long categoryId,@RequestParam Integer status){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(status!=null,Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        if(list!=null){
            return R.success(list);
        }
        return R.error("查询失败");
    }
    @Transactional
    @GetMapping("/{id}")
    public R<SetmealDto> reget(@PathVariable Long id){
        SetmealDto setmealDto=new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);
        setmealDto.setCategoryName(category.getName());
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        log.info(setmealDto.toString());
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmealwithDish(setmealDto);
        return R.success("更新成功");
    }
}
