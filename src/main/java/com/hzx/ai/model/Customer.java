package com.hzx.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户实体类
 * 
 * <p>表示航班预订系统的客户信息，包含客户基本信息和相关的航班预订记录。
 * 使用Lombok注解简化getter/setter方法。</p>
 * 
 * <p>主要属性：</p>
 * <ul>
 *   <li>客户姓名：客户标识</li>
 *   <li>预订列表：该客户的所有航班预订记录</li>
 * </ul>
 * 
 * <p>支持一个客户拥有多个航班预订，体现了一对多的关系。</p>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    /**
     * 客户姓名
     * <p>客户的真实姓名，用于身份识别和预订查询</p>
     */
    private String name;

    /**
     * 客户的航班预订列表
     * <p>存储该客户的所有航班预订记录，支持一对多关系</p>
     */
    private List<Booking> bookings = new ArrayList<>();
}