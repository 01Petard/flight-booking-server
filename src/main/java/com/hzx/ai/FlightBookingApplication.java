package com.hzx.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/**
 * 航班预订系统主应用类
 * 
 * <p>这是一个基于Spring AI的智能航班预订系统，集成了AI聊天机器人、
 * 向量数据库和RAG（检索增强生成）功能，为用户提供智能化的航班预订服务。</p>
 * 
 * <p>主要功能包括：</p>
 * <ul>
 *   <li>AI驱动的客户服务聊天机器人</li>
 *   <li>航班预订管理（查询、修改、取消）</li>
 *   <li>基于向量数据库的智能文档检索</li>
 *   <li>多模态AI模型支持（Ollama、OpenAI等）</li>
 * </ul>
 * 
 * @author xushu
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class FlightBookingApplication {

    /**
     * 应用程序入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
    }

    /**
     * 初始化向量数据库，将服务条款文档导入向量存储
     * 
     * <p>在实际生产环境中，文档导入通常会在CI服务器或类似环境中单独进行。
     * 这里为了演示目的，在应用启动时自动导入。</p>
     * 
     * @param embeddingModel 嵌入模型，用于生成文档的向量表示
     * @param vectorStore 向量存储，用于存储和检索文档向量
     * @param termsOfServiceDocs 服务条款文档资源
     * @return CommandLineRunner 用于在应用启动后执行初始化任务
     */
    @Bean
    CommandLineRunner ingestTermOfServiceToVectorStore(
            EmbeddingModel embeddingModel, 
            VectorStore vectorStore,
            @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {

        return args -> {
            // 1. 读取文档
            var documents = new TextReader(termsOfServiceDocs).read();
            
            // 2. 使用TokenTextSplitter分割文档
            var splitDocuments = new TokenTextSplitter().transform(documents);
            
            // 3. 将分割后的文档写入向量存储
            vectorStore.write(splitDocuments);
            
            System.out.println("✅ 服务条款文档已成功导入向量数据库");
        };
    }

    /**
     * 配置聊天内存管理器
     * 
     * <p>使用内存存储来维护用户对话历史，支持上下文感知的对话。</p>
     * 
     * @return ChatMemory 聊天内存实例
     */
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    /**
     * 配置向量存储
     * 
     * <p>使用简单的内存向量存储来存储文档的向量表示，支持语义搜索。</p>
     * 
     * @param embeddingModel 嵌入模型，用于生成和查询向量
     * @return VectorStore 向量存储实例
     */
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
