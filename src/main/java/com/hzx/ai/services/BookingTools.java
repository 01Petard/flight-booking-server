package com.hzx.ai.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hzx.ai.model.BookingStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * 航班预订工具配置类
 * 
 * <p>提供Spring AI函数调用的工具函数配置，支持AI聊天机器人通过函数调用
 * 执行航班预订相关的业务操作。每个函数都对应一个具体的业务功能。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>查询航班预订详情</li>
 *   <li>修改航班预订信息</li>
 *   <li>取消航班预订</li>
 * </ul>
 * 
 * <p>这些函数可以被AI聊天机器人调用，实现智能化的航班预订服务。</p>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@Configuration
public class BookingTools {

    private static final Logger logger = LoggerFactory.getLogger(BookingTools.class);

    @Autowired
    private FlightBookingService flightBookingService;

    /**
     * 获取机票预订详细信息的函数
     * 
     * <p>AI聊天机器人可以通过调用此函数来查询指定客户的航班预订详情。
     * 如果查询失败，会返回包含错误信息的响应。</p>
     * 
     * @return Function<BookingDetailsRequest, BookingDetails> 查询预订详情的函数
     */
    @Bean
    @Description("获取机票预订详细信息，需要提供预订号和客户姓名")
    public Function<BookingDetailsRequest, BookingDetails> getBookingDetails() {
        return request -> {
            try {
                logger.info("查询预订详情：预订号={}, 客户姓名={}", request.bookingNumber(), request.name());
                return flightBookingService.getBookingDetails(request.bookingNumber(), request.name());
            } catch (Exception e) {
                String errorMessage = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
                logger.warn("查询预订详情失败：{}", errorMessage);
                return new BookingDetails(
                    request.bookingNumber(), 
                    request.name(), 
                    null, 
                    null, 
                    null, 
                    null, 
                    null
                );
            }
        };
    }

    /**
     * 修改机票预订信息的函数
     * 
     * <p>AI聊天机器人可以通过调用此函数来修改客户的航班预订信息，
     * 包括航班日期、出发地和目的地。</p>
     * 
     * @return Function<ChangeBookingDatesRequest, String> 修改预订信息的函数
     */
    @Bean
    @Description("修改机票预订信息，包括日期、出发地和目的地")
    public Function<ChangeBookingDatesRequest, String> changeBooking() {
        return request -> {
            try {
                logger.info("修改预订信息：预订号={}, 客户姓名={}, 新日期={}, 出发地={}, 目的地={}", 
                    request.bookingNumber(), request.name(), request.date(), request.from(), request.to());
                
                flightBookingService.changeBooking(
                    request.bookingNumber(), 
                    request.name(), 
                    request.date(), 
                    request.from(),
                    request.to()
                );
                
                return "预订信息修改成功";
            } catch (Exception e) {
                String errorMessage = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
                logger.error("修改预订信息失败：{}", errorMessage);
                return "修改失败：" + errorMessage;
            }
        };
    }

    /**
     * 取消机票预订的函数
     * 
     * <p>AI聊天机器人可以通过调用此函数来取消客户的航班预订。
     * 根据业务规则，航班起飞前48小时内不允许取消。</p>
     * 
     * @return Function<CancelBookingRequest, String> 取消预订的函数
     */
    @Bean
    @Description("取消机票预订，需要提供预订号和客户姓名")
    public Function<CancelBookingRequest, String> cancelBooking() {
        return request -> {
            try {
                logger.info("取消预订：预订号={}, 客户姓名={}", request.bookingNumber(), request.name());
                
                flightBookingService.cancelBooking(request.bookingNumber(), request.name());
                
                return "预订取消成功";
            } catch (Exception e) {
                String errorMessage = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
                logger.error("取消预订失败：{}", errorMessage);
                return "取消失败：" + errorMessage;
            }
        };
    }

    // ==================== 请求和响应记录类 ====================

    /**
     * 查询预订详情的请求参数
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     */
    public record BookingDetailsRequest(String bookingNumber, String name) {
    }

    /**
     * 修改预订信息的请求参数
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @param date 新的航班日期
     * @param from 新的出发地
     * @param to 新的目的地
     */
    public record ChangeBookingDatesRequest(String bookingNumber, String name, String date, String from, String to) {
    }

    /**
     * 取消预订的请求参数
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     */
    public record CancelBookingRequest(String bookingNumber, String name) {
    }

    /**
     * 预订详情响应数据
     * 
     * <p>包含航班预订的完整信息，用于AI聊天机器人向用户展示预订详情。</p>
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @param date 航班日期
     * @param bookingStatus 预订状态
     * @param from 出发地
     * @param to 目的地
     * @param bookingClass 舱位等级
     */
    @JsonInclude(Include.NON_NULL)
    public record BookingDetails(
        String bookingNumber, 
        String name, 
        LocalDate date, 
        BookingStatusEnum bookingStatus,
        String from, 
        String to, 
        String bookingClass
    ) {
    }
}
