package com.hzx.ai.controller;

import com.hzx.ai.model.dto.BookingDetails;
import com.hzx.ai.services.FlightBookingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 航班预订管理控制器
 * 
 * <p>提供航班预订相关的REST API接口，包括查询预订信息等功能。
 * 支持跨域访问，便于前端应用集成。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>查询所有航班预订信息</li>
 *   <li>支持跨域访问</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Resource
    private FlightBookingService flightBookingService;

    /**
     * 获取所有航班预订信息
     * 
     * <p>返回系统中所有已确认的航班预订详情，包括预订号、客户姓名、
     * 航班日期、出发地、目的地、预订状态和舱位等级等信息。</p>
     * 
     * @return List<BookingDetails> 预订详情列表
     * 
     * @apiNote 该接口返回所有预订信息，在生产环境中应考虑分页和权限控制
     */
    @GetMapping("/list")
    public List<BookingDetails> getBookings() {
        return flightBookingService.getBookings();
    }
}
