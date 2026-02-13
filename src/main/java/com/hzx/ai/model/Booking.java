package com.hzx.ai.model;

import com.hzx.ai.model.enums.BookingCategoryEnum;
import com.hzx.ai.model.enums.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 航班预订实体类
 * 
 * <p>表示一个完整的航班预订信息，包含预订的基本信息、客户信息、
 * 航班详情和预订状态等。使用Lombok注解简化getter/setter方法。</p>
 * 
 * <p>主要属性：</p>
 * <ul>
 *   <li>预订号：唯一标识符</li>
 *   <li>航班日期：出发日期</li>
 *   <li>客户信息：预订客户详情</li>
 *   <li>出发地和目的地：航班路线</li>
 *   <li>预订状态：确认、取消等</li>
 *   <li>舱位等级：经济舱、商务舱等</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    /**
     * 航班预订号
     * <p>唯一标识一个航班预订，格式通常为数字或字母数字组合</p>
     */
    private String bookingNumber;

    /**
     * 航班出发日期
     * <p>使用LocalDate表示，支持日期计算和比较操作</p>
     */
    private LocalDate date;

    /**
     * 航班到达日期（预留字段）
     * <p>当前版本未使用，为未来扩展预留</p>
     */
    private LocalDate bookingTo;

    /**
     * 预订客户信息
     * <p>关联的客户对象，包含客户基本信息和相关预订</p>
     */
    private Customer customer;

    /**
     * 航班出发地
     * <p>机场名称或城市名称</p>
     */
    private String from;

    /**
     * 航班目的地
     * <p>机场名称或城市名称</p>
     */
    private String to;

    /**
     * 预订状态
     * <p>表示预订的当前状态，如已确认、已取消等</p>
     */
    private BookingStatusEnum bookingStatus;

    /**
     * 舱位等级
     * <p>表示预订的舱位类型，如经济舱、商务舱、头等舱等</p>
     */
    private BookingCategoryEnum bookingClass;

    /**
     * 全参数构造函数
     * 
     * <p>创建完整的航班预订对象，所有必要字段都必须提供。</p>
     * 
     * @param bookingNumber 预订号
     * @param date 航班日期
     * @param customer 客户信息
     * @param bookingStatus 预订状态
     * @param from 出发地
     * @param to 目的地
     * @param bookingClass 舱位等级
     */
    public Booking(String bookingNumber,
                   LocalDate date,
                   Customer customer,
                   BookingStatusEnum bookingStatus,
                   String from,
                   String to,
                   BookingCategoryEnum bookingClass) {
        this.bookingNumber = bookingNumber;
        this.date = date;
        this.customer = customer;
        this.bookingStatus = bookingStatus;
        this.from = from;
        this.to = to;
        this.bookingClass = bookingClass;
    }
}