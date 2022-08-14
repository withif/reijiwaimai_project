package com.hjnu.Controller;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.hjnu.Common.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import com.hjnu.Utils.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String > sendMessage(@RequestBody User user, HttpSession session){

//        RedisTemplate redisTemplate=new RedisTemplate();
//        ValueOperations operation= redisTemplate.opsForValue();
//        operation.set("验证码","1234",5, TimeUnit.MINUTES);

        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.hasLength(phone)){
            //生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code-->{}",code);

            //调用阿里云提供的API完成短信发送
           // SMSUtils.sendMessage("","",phone,code);
            //将生成的验证码存入Session
//            session.setAttribute(phone,code);//将验证码存到session中(旧)
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);//将验证码存入redis中，并设置有效期
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 登录判定
     * @param map
     * @param session
     * @return
     */
    @Transactional
    @PostMapping("login")
    public R<User > login(@RequestBody Map map,HttpSession session){
        //获取手机号
        String phone =(String) map.get("phone");
        //获取验证码
        String code =(String) map.get("code");
        //从session中获取code
//        String sessioncode =(String) session.getAttribute(phone);
         String  sessioncode =(String) redisTemplate.opsForValue().get(phone);//从redis中取数据,验证码
        //对比验证码来判断是否登录成功
        if(sessioncode!=null&&sessioncode.equals(code)){
            //如果是新用户则将手机号存入user表
            LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(lambdaQueryWrapper);
            if(one==null){
                one=new User();
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }
            //登陆成功之后，将用户id存到session中，过滤器会进行判断
            session.setAttribute("user",one.getId());
            //登录成功,如果登陆成功，则可以删除redis缓存中的数据
            redisTemplate.delete(phone);
            return R.success(one);
        }


        return R.error("登录失败");
    }
}
