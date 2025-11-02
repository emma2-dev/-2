package com.blog.service;

import com.blog.entity.Carousel;
import java.util.List;

public interface CarouselService {
    void addCarousel(Carousel carousel);
    void updateCarousel(Carousel carousel);
    void deleteCarousel(Long id);
    List<Carousel> getAllCarousels(); // 管理员查所有
    List<Carousel> getDisplayCarousels(); // 展示用查可见
    Carousel getCarouselById(Long id);
}

