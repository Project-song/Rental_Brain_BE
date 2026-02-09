package com.devoops.rentalbrain;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class RentalBrainApplicationTests {

    @DisplayName("contextLoads")
    @Test
    void contextLoads() {
    }

}
