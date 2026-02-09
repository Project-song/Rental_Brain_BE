package com.devoops.rentalbrain.customer.channel.query.controller;

import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 시큐리티 필터로 막히면 우선 끄고 진행
class ChannelQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpenAIClient openAIClient;

    @DisplayName("상태확인")
    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(get("/channel/health"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("4번 채널이 있는지 확인")
    @Test
    public void findChannelIdTest() throws Exception {

        mockMvc.perform(get("/channel/channels/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.channelId").value(4))
                .andDo(print());
    }


}