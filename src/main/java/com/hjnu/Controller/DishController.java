package com.hjnu.Controller;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.hjnu.Common.*;
import com.hjnu.DTO.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/page")
    public R<Page> pageList(@Param("page")Integer page , @Param("pageSize")Integer pageSize,@Param("name")String name){

        Page<DishDto> dtoPage=new Page<>(page,pageSize);
        Page<Dish> Dishpage=new Page<>(page,pageSize);
        //查询到常规数据，不包括菜品的类名
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.hasLength(name),Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getCreateTime);
        dishService.page(Dishpage, lambdaQueryWrapper);
        //拷贝对象,除了records对象，其他的都拷过去
        BeanUtils.copyProperties(Dishpage,dtoPage,"records");
        /**
         * 将原来的每一个Dish对象拷贝到一个新的DishDto对象中
         * 根据id查询另一个表category中的name,
         * 封装在一个DishDto中并返回
         */
         List<DishDto> DishDtolist=Dishpage.getRecords().stream().map((item)->{
                DishDto dishDto=new DishDto();
                BeanUtils.copyProperties(item,dishDto);
                //获得category类别id,即分类id
                Long id = item.getCategoryId();
                Category category = categoryService.getById(id);
                if(category!=null){
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                }
                return dishDto;
            }).collect(Collectors.toList());
         dtoPage.setRecords(DishDtolist);
        return R.success(dtoPage);

    }
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.addDishWithFlaver(dishDto);
        return R.success("新增菜品成功");
    }
    @GetMapping("/{id}")
    public R<DishDto> getDishWithflavor(@PathVariable Long id){
        DishDto dishWithFlaver = dishService.getDishWithFlaver(id);
        return R.success(dishWithFlaver);
    }

    /**
     * 执行了两张表
     * @param dishDto
     * @return
     */
    @PutMapping
   public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishWithFlavor(dishDto);
        return R.success("更新成功");
   }

    @PostMapping("/status/{status}")
   public R<String> updateDish(@RequestParam("ids")List<Long> ids,@PathVariable Integer status){
//        ids.forEach((item)->{
//            LambdaUpdateWrapper<Dish> lambdaUpdateWrapper=new LambdaUpdateWrapper();
//            lambdaUpdateWrapper.set(Dish::getStatus,status);
//            lambdaUpdateWrapper.eq(Dish::getId,item);
//            boolean update = dishService.update(lambdaUpdateWrapper);
//        });
        List<Dish> dishes = dishService.listByIds(ids);
        List<Dish> l=    dishes.stream().map((item)->{
            Dish d=new Dish();
            BeanUtils.copyProperties(item,d,"status");
            d.setStatus(status);
            return d;
        }).collect(Collectors.toList());
        dishService.updateBatchById(l);

        return R.success("更新成功");
    }

    @DeleteMapping
   public R<String> delete(@RequestParam("ids") List<Long> ids){
        dishService.removeBatchByIds(ids);
        return R.success("删除成功");
    }
//    @GetMapping("/list")
//    public R<List> getlist(Dish dish){
//        Long categoryId = dish.getCategoryId();
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(categoryId!=null,Dish::getCategoryId,categoryId);
//        lambdaQueryWrapper.eq(Dish::getStatus,1);
//        lambdaQueryWrapper.orderByAsc(Dish::getSort);
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//        if(list!=null){
//            return R.success(list);
//        }
//        return R.error("查询失败");
//    }

        @GetMapping("/list")
    public R<List> getlist(Dish dish){
        String redis_key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        List<DishDto> l=null;
        //从redis中查询数据，如果查不到，再从数据库中找
        l= (List<DishDto>)redisTemplate.opsForValue().get(redis_key);
        if(l!=null){
            return R.success(l);
        }
        Long categoryId = dish.getCategoryId();
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(categoryId!=null,Dish::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort);
        List<Dish> list = dishService.list(lambdaQueryWrapper);

        l=list.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper=new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        if(l!=null){
            redisTemplate.opsForValue().set(redis_key,l,60,TimeUnit.MINUTES);
            return R.success(l);
        }
        return R.error("查询失败");
    }
}
