package com.devoops.rentalbrain.customer.customerlist.query.controller;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest

/* 가상의 요청(request)을 테스트 하기 위한 Mock 객체 테스트용 설정(요청, 전송, 응답) */
@AutoConfigureMockMvc(addFilters = false)
class CustomerlistQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpenAIClient openAIClient;  // OpenAiConfig 빈 Mock

    @MockitoBean
    private AiCommandService aiCommandService;

    @Test
    @DisplayName("상태확인")
    public void healthCheckTest() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("1번 고객 확인")
    public void customerlistTest() throws Exception {
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andDo(print());
    }


}