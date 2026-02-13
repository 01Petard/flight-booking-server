package com.hzx.ai.model.request;

/**
 * 查询预订详情的请求参数
 *
 * @author zexiao.huang
 * @since 2026/2/13 17:37
 */
public record BookingDetailsRequest(
        // 预订号
        String bookingNumber,

        // 客户姓名
        String name
) {
}