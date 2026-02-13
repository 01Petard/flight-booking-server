package com.hzx.ai.services;

import com.hzx.ai.model.*;
import com.hzx.ai.services.BookingTools.BookingDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 航班预订服务类
 * 
 * <p>提供航班预订的核心业务逻辑，包括预订查询、修改、取消等功能。
 * 使用内存数据存储演示数据，支持基本的航班预订管理操作。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>航班预订信息查询</li>
 *   <li>航班预订信息修改</li>
 *   <li>航班预订取消</li>
 *   <li>演示数据初始化</li>
 * </ul>
 * 
 * <p>业务规则：</p>
 * <ul>
 *   <li>航班起飞前24小时内不允许修改</li>
 *   <li>航班起飞前48小时内不允许取消</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class FlightBookingService {

    private final BookingData db;

    /**
     * 构造函数，初始化演示数据
     */
    public FlightBookingService() {
        this.db = new BookingData();
        initDemoData();
    }

    /**
     * 初始化演示数据
     * 
     * <p>创建示例客户和航班预订数据，用于演示系统功能。
     * 包含5个客户的航班预订信息。</p>
     */
    private void initDemoData() {
        // 示例客户姓名
        List<String> names = List.of("徐庶", "诸葛", "百里", "楼兰", "庄周");
        
        // 示例机场代码
        List<String> airportCodes = List.of(
            "北京", "上海", "广州", "深圳", "杭州", "南京", 
            "青岛", "成都", "武汉", "西安", "重庆", "大连", "天津"
        );
        
        Random random = new Random();
        var customers = new ArrayList<Customer>();
        var bookings = new ArrayList<Booking>();

        // 为每个客户创建航班预订
        for (int i = 0; i < 5; i++) {
            String name = names.get(i);
            String from = airportCodes.get(random.nextInt(airportCodes.size()));
            String to = airportCodes.get(random.nextInt(airportCodes.size()));
            BookingCategoryEnum bookingClass = BookingCategoryEnum.values()[random.nextInt(BookingCategoryEnum.values().length)];
            
            // 创建客户
            Customer customer = new Customer();
            customer.setName(name);

            // 设置航班日期（从今天开始，每隔2天一个航班）
            LocalDate date = LocalDate.now().plusDays(2 * (i + 1));

            // 创建航班预订
            Booking booking = new Booking(
                "10" + (i + 1),  // 预订号
                date,             // 航班日期
                customer,         // 客户信息
                    BookingStatusEnum.CONFIRMED, // 预订状态
                from,             // 出发地
                to,               // 目的地
                bookingClass      // 舱位等级
            );
            
            customer.getBookings().add(booking);

            customers.add(customer);
            bookings.add(booking);
        }

        // 重置数据库数据
        db.setCustomers(customers);
        db.setBookings(bookings);
        
        System.out.println("✅ 演示数据初始化完成，共创建 " + customers.size() + " 个客户，" + bookings.size() + " 个航班预订");
    }

    /**
     * 获取所有已确认的航班预订
     * 
     * @return List<BookingDetails> 预订详情列表
     */
    public List<BookingDetails> getBookings() {
        return db.getBookings().stream()
                .map(this::toBookingDetails)
                .toList();
    }

    /**
     * 根据预订号和客户姓名查找航班预订
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @return Booking 航班预订信息
     * @throws IllegalArgumentException 当预订不存在时抛出异常
     */
    private Booking findBooking(String bookingNumber, String name) {
        return db.getBookings()
                .stream()
                .filter(b -> b.getBookingNumber().equalsIgnoreCase(bookingNumber))
                .filter(b -> b.getCustomer().getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("未找到预订号为 %s，客户姓名为 %s 的航班预订", bookingNumber, name)
                ));
    }

    /**
     * 查询航班预订详情
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @return BookingDetails 预订详情
     * @throws IllegalArgumentException 当预订不存在时抛出异常
     */
    public BookingDetails getBookingDetails(String bookingNumber, String name) {
        var booking = findBooking(bookingNumber, name);
        return toBookingDetails(booking);
    }

    /**
     * 修改航班预订信息
     * 
     * <p>支持修改航班日期、出发地和目的地。根据业务规则，
     * 航班起飞前24小时内不允许修改。</p>
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @param newDate 新的航班日期（格式：yyyy-MM-dd）
     * @param from 新的出发地
     * @param to 新的目的地
     * @throws IllegalArgumentException 当不满足修改条件时抛出异常
     */
    public void changeBooking(String bookingNumber, String name, String newDate, String from, String to) {
        var booking = findBooking(bookingNumber, name);
        
        // 检查是否可以修改（航班起飞前24小时内不允许修改）
        if (booking.getDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("航班起飞前24小时内不允许修改预订信息");
        }
        
        // 更新预订信息
        booking.setDate(LocalDate.parse(newDate));
        booking.setFrom(from);
        booking.setTo(to);

        System.out.printf("✅ 预订 %s 修改成功，新日期：%s，出发地：%s，目的地：%s%n",
                bookingNumber, newDate, from, to);
    }

    /**
     * 取消航班预订
     * 
     * <p>根据业务规则，航班起飞前48小时内不允许取消预订。</p>
     * 
     * @param bookingNumber 预订号
     * @param name 客户姓名
     * @throws IllegalArgumentException 当不满足取消条件时抛出异常
     */
    public void cancelBooking(String bookingNumber, String name) {
        var booking = findBooking(bookingNumber, name);
        
        // 检查是否可以取消（航班起飞前48小时内不允许取消）
        if (booking.getDate().isBefore(LocalDate.now().plusDays(2))) {
            throw new IllegalArgumentException("航班起飞前48小时内不允许取消预订");
        }
        
        // 更新预订状态为已取消
        booking.setBookingStatus(BookingStatusEnum.CANCELLED);

        System.out.printf("✅ 预订 %s 取消成功%n", bookingNumber);
    }

    /**
     * 将Booking对象转换为BookingDetails对象
     * 
     * @param booking 航班预订对象
     * @return BookingDetails 预订详情对象
     */
    private BookingDetails toBookingDetails(Booking booking) {
        return new BookingDetails(
            booking.getBookingNumber(),           // 预订号
            booking.getCustomer().getName(),      // 客户姓名
            booking.getDate(),                    // 航班日期
            booking.getBookingStatus(),           // 预订状态
            booking.getFrom(),                    // 出发地
            booking.getTo(),                      // 目的地
            booking.getBookingClass().toString()  // 舱位等级
        );
    }
}
