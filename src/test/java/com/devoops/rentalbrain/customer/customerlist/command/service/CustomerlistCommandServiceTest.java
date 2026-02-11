package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerlistQueryMapper;
import com.openai.client.OpenAIClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class CustomerlistCommandServiceTest {

    private final CustomerlistCommandService customerlistCommandService;
    private final CustomerlistCommandRepository customerlistCommandRepository;

    @Autowired
    CustomerlistCommandServiceTest(CustomerlistCommandService customerlistCommandService, CustomerlistCommandServiceImpl customerlistCommandServiceImpl, CustomerlistCommandRepository customerlistCommandRepository, CustomerlistQueryMapper customerlistQueryMapper) {
        this.customerlistCommandService = customerlistCommandService;
        this.customerlistCommandRepository = customerlistCommandRepository;
    }

    @MockitoBean
    private OpenAIClient openAIClient;  // OpenAiConfig 빈 Mock

    @MockitoBean
    private AiCommandService aiCommandService;

    private Long savedCustomerId;
    private String beforeCustomerCode;


    @BeforeEach
    void setUp() {
        CustomerlistCommandDTO dto = new CustomerlistCommandDTO();
        dto.setName("고객1");
        dto.setInCharge("상담사");
        dto.setCallNum("01000000000");
        dto.setEmail("customer1@customer.customer");
        dto.setBusinessNum("2000000000");
        dto.setAddr("서울");
        dto.setSegmentId(1L);
        dto.setChannelId(1L);

        savedCustomerId = customerlistCommandService.registerCustomer(dto);

        CustomerlistCommandEntity before =
                customerlistCommandRepository.findById(savedCustomerId).orElseThrow();

        beforeCustomerCode = before.getCustomerCode();
    }


    @Test
    @DisplayName("고객 등록 테스트(customer_code는 자동 생성, is_deleted='N'")
    void registerCustomer() throws Exception {

        // given
        CustomerlistCommandDTO dto = new CustomerlistCommandDTO();
        dto.setName("고객2");
        dto.setInCharge("상담사");
        dto.setCallNum("01000000000");
        dto.setEmail("customer2@customer.customer");
        dto.setBusinessNum("2000000000");
        dto.setAddr("서울");
        dto.setSegmentId(1L);
        dto.setChannelId(1L);

        // when
        savedCustomerId = customerlistCommandService.registerCustomer(dto);

        // then id 반환
        assertThat(savedCustomerId).isNotNull().isPositive();

        // then DB 저장 확인
        CustomerlistCommandEntity saved = customerlistCommandRepository.findById(savedCustomerId)
                .orElseThrow(() -> new AssertionError("저장된 고객이 DB에서 조회되지 않음"));

        assertThat(saved.getCustomerCode()).isNotBlank(); // 코드 생성
        assertThat(saved.getIsDeleted()).isEqualTo("N");  // 기본값

        // then (4) 서비스 기본값 적용 검증 (DB에 FK가 있어야 성공)
        assertThat(saved.getSegmentId()).isEqualTo(1L);
        assertThat(saved.getChannelId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("고객 조회 테스트")
    void selectCustomer() throws Exception {

        // when(여기서 given 은 BeforeEach에 있음)
        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(savedCustomerId)
                .orElseThrow(()-> new AssertionError("존재하지 않는 고객입니다."));

        // then
        assertThat(customer.getId()).isEqualTo(savedCustomerId);
        assertThat(customer.getName()).isEqualTo("고객1");
    }


    @Test
    @DisplayName("고객 수정 테스트")
    void updateCustomer() {

        // given
        CustomerlistCommandDTO updateDto = new CustomerlistCommandDTO();
        updateDto.setName("고객1수정");

        // when
        customerlistCommandService.updateCustomer(savedCustomerId, updateDto);

        // then
        CustomerlistCommandEntity after =
                customerlistCommandRepository.findById(savedCustomerId)
                        .orElseThrow(()-> new AssertionError("존재하지 않는 고객입니다."));


        assertThat(after.getName()).isEqualTo("고객1수정");
        assertThat(after.getCustomerCode()).isEqualTo(beforeCustomerCode);
    }

    @Test
    @DisplayName("고객 삭제 테스트")
    void deleteCustomer() {

        // when
        customerlistCommandService.deleteCustomer(savedCustomerId);

        // then
        CustomerlistCommandEntity after = customerlistCommandRepository.findById(savedCustomerId)
                .orElseThrow(()-> new AssertionError("존재하지 않는 고객입니다."));

        assertThat(after.getIsDeleted()).isEqualTo("Y");

        assertThat(after.getCustomerCode()).isEqualTo(beforeCustomerCode);
    }



}
