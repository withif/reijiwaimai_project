package com.hjnu.Controller;

import com.hjnu.Common.*;
import com.hjnu.Pojo.*;
import com.hjnu.Service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String > submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("支付成功");
    }
}
