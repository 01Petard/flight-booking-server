package com.hzx.ai.model.request;

/**
 * 修改预订信息的请求参数
 *
 * @author zexiao.huang
 * @since 2026/2/13 17:38
 */
public record ChangeBookingDatesRequest(
        // 预订号
        String bookingNumber,

        // 客户姓名
        String name,

        // 新的航班日期
        String date,

        // 新的出发地
        String from,

        // 新的目的地
        String to

) {
}