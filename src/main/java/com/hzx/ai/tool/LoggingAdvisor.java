package com.hzx.ai.tool;

import org.springframework.ai.chat.client.RequestResponseAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 日志记录顾问类
 * 
 * <p>实现Spring AI的RequestResponseAdvisor接口，用于记录AI聊天请求和响应的日志信息。
 * 帮助开发者和运维人员监控AI系统的运行状态和性能。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>记录AI聊天请求的详细信息</li>
 *   <li>支持日志级别配置</li>
 *   <li>便于问题排查和性能分析</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
public class LoggingAdvisor implements RequestResponseAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvisor.class);

    /**
     * 处理请求前的日志记录
     * 
     * <p>在AI聊天请求执行前记录请求信息，包括请求内容和上下文信息。
     * 返回原始请求对象，不进行修改。</p>
     * 
     * @param request 建议的请求对象
     * @param context 请求上下文信息
     * @return AdvisedRequest 原始请求对象
     */
    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        logger.info("AI聊天请求：{}", request);
        logger.debug("请求上下文：{}", context);
        return request;
    }

    /**
     * 获取顾问的执行顺序
     * 
     * <p>返回0表示该顾问具有最高优先级，会在其他顾问之前执行。
     * 这确保了日志记录在请求处理的最早期进行。</p>
     * 
     * @return int 执行顺序，数值越小优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}