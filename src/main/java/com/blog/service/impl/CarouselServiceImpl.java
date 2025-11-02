package com.blog.service.impl;

import com.blog.entity.Carousel;
import com.blog.mapper.CarouselMapper;
import com.blog.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public void addCarousel(Carousel carousel) {
        carouselMapper.insert(carousel);
    }

    @Override
    public void updateCarousel(Carousel carousel) {
        carouselMapper.update(carousel);
    }

    @Override
    public void deleteCarousel(Long id) {
        carouselMapper.deleteById(id);
    }

    @Override
    public List<Carousel> getAllCarousels() {
        return carouselMapper.selectAll();
    }

    @Override
    public List<Carousel> getDisplayCarousels() {
        return carouselMapper.selectDisplay();
    }

    @Override
    public Carousel getCarouselById(Long id) {
        return carouselMapper.selectById(id);
    }
}

