package com.devoops.rentalbrain;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RentalBrainApplicationTests {

    @MockitoBean
    private OpenAIClient openAIClient;  // OpenAiConfig 빈 Mock

    @MockitoBean
    private AiCommandService aiCommandService;

    @Test
    void contextLoads() {
    }
}
