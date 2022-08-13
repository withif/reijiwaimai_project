package com.hjnu.Service;

import com.baomidou.mybatisplus.extension.service.*;
import com.hjnu.Pojo.*;
import org.springframework.stereotype.*;

@Service
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
