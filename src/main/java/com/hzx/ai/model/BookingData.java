package com.hzx.ai.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 航班预订数据存储类
 * 
 * <p>作为系统的内存数据存储，用于存储客户信息和航班预订信息。
 * 在演示环境中替代数据库，提供基本的数据持久化功能。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>存储客户信息列表</li>
 *   <li>存储航班预订信息列表</li>
 *   <li>支持数据的增删改查操作</li>
 * </ul>
 * 
 * <p>注意：这是一个内存存储实现，应用重启后数据会丢失。
 * 在生产环境中应该使用数据库进行持久化存储。</p>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class BookingData {

    /**
     * 客户信息列表
     * <p>存储系统中所有注册客户的信息</p>
     */
    private List<Customer> customers = new ArrayList<>();

    /**
     * 航班预订信息列表
     * <p>存储系统中所有航班预订的详细信息</p>
     */
    private List<Booking> bookings = new ArrayList<>();
}
