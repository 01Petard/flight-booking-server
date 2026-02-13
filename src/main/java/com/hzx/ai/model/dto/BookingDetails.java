package com.hzx.ai.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hzx.ai.model.enums.BookingStatusEnum;

import java.time.LocalDate;

/**
 * 预订详情响应数据
 *
 * <p>包含航班预订的完整信息，用于AI聊天机器人向用户展示预订详情。</p>
 *
 * @author zexiao.huang
 * @since 2026/2/13 17:47
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDetails(

        // 预订号
        String bookingNumber,

        // 客户姓名
        String name,

        // 航班日期
        LocalDate date,

        // 预订状态
        BookingStatusEnum bookingStatus,

        // 出发地
        String from,

        // 目的地
        String to,

        // 舱位等级
        String bookingClass
) {
}