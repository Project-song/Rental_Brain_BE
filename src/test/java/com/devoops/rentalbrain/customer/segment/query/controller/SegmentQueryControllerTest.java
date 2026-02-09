package com.devoops.rentalbrain.customer.segment.query.controller;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.common.ai.query.service.AiQueryService;
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
// JWT 필터 끄기
@AutoConfigureMockMvc(addFilters = false)
class SegmentQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AiQueryService aiQueryService;

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("이탈위험고객 세그먼트 확인")
    @Test
    public void findSegmentIdTest() throws Exception {

        mockMvc.perform(get("/segment/list/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.segmentId").value(4))
                .andDo(print());

    }

}