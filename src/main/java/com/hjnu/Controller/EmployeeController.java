package com.hjnu.Controller;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.mapper.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.hjnu.Common.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.nio.charset.*;
import java.time.*;

/**
 * @author 36017
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController  {

    @Autowired
    private  EmployeeService employeeService;

    /**
     * 登录
     * @param employee
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session){
        //对密码进行md5加密
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        //查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(employee.getUsername()!=null&&!("".equals(employee.getUsername())),Employee::getUsername ,employee.getUsername());
        Employee one = employeeService.getOne(lambdaQueryWrapper);
        //判断是否查询到数据
        if(one==null){
            return R.error("用户名错误!");
        }
        if(!password.equals(one.getPassword())){
            return R.error("密码输入错误!");
        }
        if(one.getStatus()==1){
            session.setAttribute("employee_id",one.getId());
            return R.success(one);
        }else {
            return R.error("该用户已被禁止使用!");
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("employee_id");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee,HttpSession session){
        //获取当前用户的id
        Long UserID=(Long)session.getAttribute("employee_id");
        //将从前端获取的密码进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /**
         * 公共字段设置为自动填充
         */
//        employee.setCreateTime(LocalDateTime.now());//设置被新增员工创建的时间
//        employee.setUpdateTime(LocalDateTime.now());//设置被新增员工更新的时间
//        employee.setCreateUser(UserID);//创建人的id(当前用户的id)
//        employee.setUpdateUser(UserID);//更新人的id(当前用户的id)
        try {
            employeeService.save(employee);
        }catch (Exception e){
            R.error("添加用户失败");
        }
        log.info("新增员工成功");
        return R.success("新增员工成功");
    }

    /**
     * 分页条件查询需要分页插件:PaginationInnerInterceptor(创建一个配置类，如：com.hjnu.config。MyBatisPlusConfig)
     * 以下两个请求是同一个路径，但是参数不同,都来自于：/backend/index.html 这个页面
     * 请求一：http://localhost/employee/page?page=1&pageSize=10&name=abc
     * 请求二： http://localhost/employee/page?page=1&pageSize=10
     * @return
     */
    @GetMapping("/page")
    public R<Page> pagelist(int page,int pageSize,String name){ //如果传参和变量不一样，则可以使用@Param("page")来将其一一对应
        log.info("得到的参数有==>page={} pageSzie={} name={}",page,pageSize,name);
        //分页构造器:page表示第几页，pageSize表示每一页的大小（条数）
        Page pageList=new Page(page,pageSize);
        //条件构造器,和过滤条件
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasLength(name),Employee::getName,name);
        lambdaQueryWrapper.orderByAsc(Employee::getName);
        //执行sql语句，分页查询的结果会封装在Page中
        employeeService.page(pageList,lambdaQueryWrapper);
        return R.success(pageList);
    }

    /**
     * 用户更新
     * @param employee
     * @param session
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpSession session){
        Long employee_id = (Long)session.getAttribute("employee_id");
        /**
         * 设置公共字段自动填充
         */
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(employee_id);
        long thread_id = Thread.currentThread().getId();
        log.info("当前线程id为{}",thread_id);
        boolean save = employeeService.updateById(employee);
        if(save){
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }
    /**
     * 根据id查询员工
     * 页面跳转(携带参数)：  http://127.0.0.1/backend/page/member/add.html?id=1556688703290343425
     * 发送请求（rest风格）：  http://127.0.0.1/employee/1556688703290343425
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("查询失败");
    }


}
