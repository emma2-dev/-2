package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.Carousel;
import com.blog.service.CarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/carousel")
@CrossOrigin
public class CarouselController {

    private static final Logger logger = LoggerFactory.getLogger(CarouselController.class);

    @Autowired
    private CarouselService carouselService;

    /**
     * 管理员新增轮播图
     */
    @PostMapping
    public Result<String> addCarousel(HttpServletRequest request, @RequestBody Carousel carousel) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.error(403, "只有管理员才能操作");
        }
        carouselService.addCarousel(carousel);
        return Result.success("添加成功");
    }

    /**
     * 管理员修改轮播图
     */
    @PutMapping
    public Result<String> updateCarousel(HttpServletRequest request, @RequestBody Carousel carousel) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.error(403, "只有管理员才能操作");
        }
        carouselService.updateCarousel(carousel);
        return Result.success("修改成功");
    }

    /**
     * 管理员删除轮播图
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCarousel(HttpServletRequest request, @PathVariable Long id) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.error(403, "只有管理员才能操作");
        }
        carouselService.deleteCarousel(id);
        return Result.success("删除成功");
    }

    /**
     * 管理员查看所有轮播图
     */
    @GetMapping("/all")
    public Result<List<Carousel>> getAllCarousels(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return Result.error(403, "仅管理员可见");
        }
        return Result.success(carouselService.getAllCarousels());
    }

    /**
     * 用户/游客-首页展示轮播图（status=1）
     */
    @GetMapping("/list")
    public Result<List<Carousel>> getDisplayCarousels() {
        logger.info("收到轮播图列表请求");
        List<Carousel> carousels = carouselService.getDisplayCarousels();
        logger.info("返回轮播图数量: {}", carousels.size());
        return Result.success(carousels);
    }

}

