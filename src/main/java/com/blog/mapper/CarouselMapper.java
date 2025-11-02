package com.blog.mapper;

import com.blog.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CarouselMapper {
    int insert(Carousel carousel);
    int update(Carousel carousel);
    int deleteById(@Param("id") Long id);
    Carousel selectById(@Param("id") Long id);
    List<Carousel> selectAll();
    List<Carousel> selectDisplay(); // 查询status=1显示的轮播图
}

