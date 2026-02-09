package com.devoops.rentalbrain.customer.channel.query.controller;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.service.ChannelQueryService;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers=ChannelQueryController.class)
@AutoConfigureMockMvc(addFilters = false) // 시큐리티 필터로 막히면 우선 끄고 진행
class ChannelQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelQueryService channelQueryService;

    @MockitoBean
    private OpenAIClient openAIClient;

    @DisplayName("상태확인")
    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(get("/channel/health"))
                .andExpect(status().isOk())
                .andDo(print());

        verifyNoMoreInteractions(channelQueryService);
    }

    @DisplayName("4번 채널이 있는지 확인")
    @Test
    void findChannelIdTest() throws Exception {
        // given
        ChannelQueryDTO dto = new ChannelQueryDTO(4L, "SNS");
        given(channelQueryService.findChannelByChannelId(4)).willReturn(dto); // ✅ Integer 매칭

        // when & then
        mockMvc.perform(get("/channel/channels/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                // content-type은 print 보고 나서 다시 걸자
                .andExpect(jsonPath("$.channelId").value(4))
                .andExpect(jsonPath("$.channelName").value("SNS"));

        verify(channelQueryService).findChannelByChannelId(4);
        verifyNoMoreInteractions(channelQueryService);
    }


}