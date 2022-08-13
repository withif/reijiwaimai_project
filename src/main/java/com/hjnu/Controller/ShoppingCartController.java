package com.hjnu.Controller;

import ch.qos.logback.core.util.*;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.hjnu.Common.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("传过来的参数是=>{}",shoppingCart.toString());
        Long userId = BaseContext.getUserId();//获得user_id
        shoppingCart.setUserId(userId);
        /**
         * 判断是套餐还是菜品,判断前端传过来的菜的数量
         */
//        Long dishId = shoppingCart.getDishId();
//        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        if(shoppingCart.getSetmealId()!=null){
            //是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            //是菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        if(one!=null){
            //购物车里面已经有这个了，则数量加一
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            //购物车里面没有这个，新增一个,并设置数量为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);
    }

    /**
     * 根据user_id查询用户的购物车
     * @return
     */
    @GetMapping("/list")
    public R<List> list(){
        log.info("查看购物车...");
        Long userId = BaseContext.getUserId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 菜品或套餐数量减一
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String > sub(@RequestBody ShoppingCart shoppingCart){
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper=new LambdaUpdateWrapper();
        lambdaUpdateWrapper.eq(ShoppingCart::getUserId,BaseContext.getUserId());
        if(shoppingCart.getSetmealId()!=null){
            //是套餐
            lambdaUpdateWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            //是菜品
            lambdaUpdateWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaUpdateWrapper);
        Integer number = one.getNumber();
        if(number==1){
            shoppingCartService.remove(lambdaUpdateWrapper);
        }else {
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }
        return R.success("更新成功");
    }
    @DeleteMapping("/clean")
    public R<String > clean(){
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ShoppingCart::getUserId,BaseContext.getUserId());
        shoppingCartService.remove(lambdaUpdateWrapper);
        return R.success("清空购物车成功");
    }
}
