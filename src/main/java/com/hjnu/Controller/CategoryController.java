package com.hjnu.Controller;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.hjnu.Common.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * GET与@RequestParam
 * POST与@RequestBody
 * GET请求中，因为没有HttpEntity，所以@RequestBody并不适用。(一般情况)
 * POST请求传的是JSON数据时，需要加上@RequestBody注解
 * POST请求中，通过HttpEntity传递的参数，必须要在请求头中声明数据的类型Content-Type，SpringMVC通过使用
 * HandlerAdapter 配置的HttpMessageConverters来解析HttpEntity中的数据，然后绑定到相应的bean上。
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类或套餐
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        if(save){
            return R.success(category.getName()+"新增成功");
        }
        return R.error(category.getName()+"新增失败");
    }

    /**
     * 查询Category列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageList(@Param("page")Integer page,@Param("pageSize")Integer pageSize,@Param("name")String name){
        log.info("查询所有分类信息");
        Page pagelist=new Page(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.hasLength(name),Category::getName,name);
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        Page pages = categoryService.page(pagelist, lambdaQueryWrapper);
        if(pages!=null){
            return R.success(pagelist);
        }
        return R.error("查询失败");

    }
    @DeleteMapping
    public R<String > del(@Param("id")Long id){
        log.info("删除分类信息");
        categoryService.remove(id);
        return R.success("删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息...");
        boolean update = categoryService.updateById(category);
        if(update){
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }
//    @GetMapping("/list")
//    public R<List<Category>> getList(Category category){
//        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper();
//        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType,category.getType());
//        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
//        List<Category> list = categoryService.list(lambdaQueryWrapper);
//        if(list!=null){
//            return R.success(list);
//        }
//        return R.error("查询失败");
//    }

        @GetMapping("/list")
    public R<List<Category>> getList(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        if(list!=null){
            return R.success(list);
        }
        return R.error("查询失败");
    }
}
