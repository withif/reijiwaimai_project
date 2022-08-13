package com.hjnu.Mapper;

import com.baomidou.mybatisplus.core.mapper.*;
import com.hjnu.Pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.*;

@Mapper
@Component
public interface DishMapper extends BaseMapper<Dish> {
}
