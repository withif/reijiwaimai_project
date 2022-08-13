package com.hjnu.Service;

import com.baomidou.mybatisplus.extension.service.*;
import com.hjnu.Pojo.*;
import org.springframework.stereotype.*;

@Service
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
