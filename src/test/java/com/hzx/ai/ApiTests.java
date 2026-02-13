package com.hzx.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest(classes = FlightBookingApplication.class)
class ApiTests {

    @Resource
    private CustomerSupportAssistant assistant;


    @Test
    void contextLoads() {

        Flux<String> chat = assistant.chat("chat_memory_conversation_id", "chat_memory_response_size");
        System.out.println(chat);

    }

}
