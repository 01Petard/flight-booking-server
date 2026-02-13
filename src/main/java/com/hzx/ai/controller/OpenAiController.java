package com.hzx.ai.controller;

import com.hzx.ai.tool.LoggingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * AI智能客服控制器
 *
 * <p>提供基于Spring AI的智能客服聊天功能，集成多种AI模型支持，
 * 包括Ollama、OpenAI等。支持流式响应、聊天记忆、RAG检索增强生成
 * 和函数调用等高级功能。</p>
 *
 * <p>主要特性：</p>
 * <ul>
 *   <li>智能客服对话，支持中文交互</li>
 *   <li>流式响应，实时显示AI回复</li>
 *   <li>聊天记忆，保持对话上下文</li>
 *   <li>RAG检索，基于知识库回答问题</li>
 *   <li>函数调用，支持航班预订操作</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class OpenAiController {

    private final ChatClient chatClient;

    /**
     * 构造函数，初始化AI聊天客户端
     * <p>配置AI聊天机器人的系统提示词、顾问组件和函数调用能力。
     * 支持聊天记忆、RAG检索和日志记录等功能。</p>
     * @param chatClientBuilder 聊天客户端构建器
     * @param vectorStore       向量存储，用于RAG检索
     * @param chatMemory        聊天记忆管理器
     */
    public OpenAiController(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            ChatMemory chatMemory
    ) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        您是"图灵航空"公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                        您正在通过在线聊天系统与客户互动。
                        
                        在提供有关预订或取消预订的信息之前，您必须始终从用户处获取以下信息：
                        - 预订号
                        - 客户姓名
                        
                        在询问用户之前，请检查消息历史记录以获取此信息。
                        在更改或退订之前，请先获取预订信息并且告知条款，待用户回复确定之后才进行更改或退订的function-call。
                        
                        请讲中文。
                        今天的日期是 %s。
                        """.formatted(LocalDateTime.now()))
                .defaultAdvisors(
                        // 聊天记忆顾问
                        new PromptChatMemoryAdvisor(chatMemory),
                        // RAG检索顾问
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.query("预定航班")),
                        // 日志记录顾问
                        new LoggingAdvisor()
                )
                // 函数调用
                .defaultFunctions("getBookingDetails", "changeBooking", "cancelBooking")
                .build();
        log.info("AI客服已创建！");
    }

    /**
     * 流式AI对话接口
     * <p>提供实时流式响应的AI对话功能，支持字符级别的流式输出。
     * 使用text/plain响应类型确保流式传输正常工作。</p>
     * @param message 用户输入的消息内容
     * @return Flux<String> 流式响应内容
     * @apiNote 响应格式为text/plain，支持实时流式显示
     */
    @GetMapping(value = "/chat/stream", produces = "text/plain;charset=utf-8")
    public Flux<String> generateStreamAsString(
            @RequestParam(value = "message", defaultValue = "你好，请介绍一下图灵航空") String message
    ) {

        // 获取流式响应
        Flux<String> content = chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .user(message)
                .stream()
                .content();

        // 在流式响应结束后添加完成标记
        return content
                .onErrorResume(error -> {
                    // 错误处理，返回错误信息
                    return Flux.just("❌ 抱歉，AI服务出现错误：" + error.getMessage());
                })
                .concatWith(Flux.just("[complete]"));
    }

    /**
     * 同步AI对话接口
     *
     * <p>提供同步响应的AI对话功能，等待AI完整回复后一次性返回结果。
     * 适用于不需要实时显示的场景。</p>
     *
     * @param message 用户输入的消息内容
     * @return String AI的完整回复内容
     *
     * @apiNote 该接口会等待AI完整回复后返回，响应时间可能较长
     */
    @GetMapping("/chat")
    public String chat(
            @RequestParam(value = "message", defaultValue = "你好，请介绍一下图灵航空") String message
    ) {

        return chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .user(message)
                .call()
                .content();
    }
}
