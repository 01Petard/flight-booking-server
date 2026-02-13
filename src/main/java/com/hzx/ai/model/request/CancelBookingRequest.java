package com.hzx.ai.model.request;

/**
 * 取消预订的请求参数
 *
 * @author zexiao.huang
 * @since 2026/2/13 17:39
 */
public record CancelBookingRequest(

        // 预订号
        String bookingNumber,

        // 客户姓名
        String name

) {
}