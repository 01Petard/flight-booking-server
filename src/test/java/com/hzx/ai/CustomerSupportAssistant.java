package com.hzx.ai;

import com.hzx.ai.tool.LoggingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 客户支持助手服务类
 *
 * <p>提供基于Spring AI的智能客户支持服务，集成多种AI功能：
 * 聊天记忆、RAG检索增强生成、函数调用等。支持多轮对话和上下文感知。</p>
 *
 * <p>主要特性：</p>
 * <ul>
 *   <li>智能客服对话，支持中文交互</li>
 *   <li>聊天记忆管理，维护对话上下文</li>
 *   <li>RAG检索，基于知识库回答问题</li>
 *   <li>函数调用，支持航班预订操作</li>
 *   <li>日志记录，便于问题追踪</li>
 * </ul>
 *
 * <p>系统提示词配置了完整的客户服务流程，确保在提供预订相关服务前
 * 获取必要的客户信息，并遵循相应的业务规则。</p>
 *
 * @author Christian Tzolov
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@Slf4j
public class CustomerSupportAssistant {

    private final ChatClient chatClient;

    /**
     * 构造函数，初始化AI聊天客户端
     *
     * <p>配置AI聊天机器人的系统提示词、顾问组件和函数调用能力。
     * 支持聊天记忆、RAG检索和日志记录等功能。</p>
     *
     * @param modelBuilder 聊天客户端构建器
     * @param vectorStore 向量存储，用于RAG检索
     * @param chatMemory 聊天记忆管理器
     */
    public CustomerSupportAssistant(ChatClient.Builder modelBuilder, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = modelBuilder
                .defaultSystem("""
                    您是"图灵航空"公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                    您正在通过在线聊天系统与客户互动。
                    
                    在提供有关预订或取消预订的信息之前，您必须始终从用户处获取以下信息：
                    - 预订号
                    - 客户姓名
                    
                    在询问用户之前，请检查消息历史记录以获取此信息。
                    在更改预订之前，您必须确保条款允许这样做。
                    如果更改需要收费，您必须在继续之前征得用户同意。
                    
                    使用提供的功能获取预订详细信息、更改预订和取消预订。
                    如果需要，可以调用相应函数调用完成辅助动作。
                    
                    请讲中文。
                    今天的日期是 %s。
                    """.formatted(LocalDateTime.now()))
                .defaultAdvisors(
                        // 聊天记忆顾问
                        new PromptChatMemoryAdvisor(chatMemory),
                        // RAG检索顾问
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()),
                        // 日志记录顾问
                        new LoggingAdvisor()
                )
                // 函数调用
                .defaultFunctions("getBookingDetails", "changeBooking", "cancelBooking")
                .build();
        log.info("初始化AI聊天客户端！");
    }

    /**
     * 执行AI对话
     *
     * <p>根据聊天ID和用户消息内容，执行AI对话并返回流式响应。
     * 支持聊天记忆，能够根据对话历史提供上下文相关的回复。</p>
     *
     * @param chatId 聊天会话ID，用于区分不同的对话会话
     * @param userMessageContent 用户输入的消息内容
     * @return Flux<String> 流式AI回复内容
     *
     * @apiNote 该方法使用流式响应，支持实时显示AI回复内容
     */
    public Flux<String> chat(String chatId, String userMessageContent) {

        return this.chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .user(userMessageContent)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content();
    }



}