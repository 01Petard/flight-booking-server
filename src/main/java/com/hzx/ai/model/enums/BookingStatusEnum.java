package com.hzx.ai.model.enums;

/**
 * 航班预订状态枚举
 * 
 * <p>定义航班预订系统中所有可能的预订状态，用于跟踪预订的生命周期。
 * 每个状态都有明确的业务含义和转换规则。</p>
 * 
 * <p>状态说明：</p>
 * <ul>
 *   <li>CONFIRMED：已确认，预订成功且已确认</li>
 *   <li>COMPLETED：已完成，航班已起飞并完成</li>
 *   <li>CANCELLED：已取消，预订被取消</li>
 * </ul>
 * 
 * <p>状态转换规则：</p>
 * <ul>
 *   <li>CONFIRMED → COMPLETED：航班起飞后自动转换</li>
 *   <li>CONFIRMED → CANCELLED：客户主动取消或系统取消</li>
 *   <li>其他状态转换：根据具体业务规则确定</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
public enum BookingStatusEnum {

    /**
     * 已确认
     * <p>预订成功且已确认，客户可以正常使用该预订</p>
     */
    CONFIRMED,

    /**
     * 已完成
     * <p>航班已起飞并完成，预订状态结束</p>
     */
    COMPLETED,

    /**
     * 已取消
     * <p>预订被取消，客户无法使用该预订</p>
     */
    CANCELLED
}
