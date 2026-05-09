package ru.netology.moneytransferservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.moneytransferservice.logger.TransferLogger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoneyTransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferLogger transferLogger;

    @Test
    void transferMoney_shouldReturnOperationId() throws Exception {
        // Настраиваем мок, чтобы логгер не падал при вызове
        doNothing().when(transferLogger).logTransfer(any(), any(), any(), anyDouble(), any());

        // Запрос строго по OpenAPI спецификации
        String requestBody = """
            {
                "cardFromNumber": "5555555555555555",
                "cardFromValidTill": "12/25",
                "cardFromCVV": "123",
                "cardToNumber": "4444444444444444",
                "amount": {
                    "value": 1000,
                    "currency": "RUB"
                }
            }
            """;

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").isNotEmpty());
    }

    @Test
    void transferMoney_withInvalidData_shouldReturnBadRequest() throws Exception {
        // Пустой объект должен вернуть 400 из-за @Valid и @NotBlank/@NotNull в DTO
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmOperation_shouldReturnOperationId() throws Exception {
        doNothing().when(transferLogger).logTransfer(any(), any(), any(), anyDouble(), any());

        String requestBody = """
            {
                "operationId": "test-uuid-123",
                "code": "1234"
            }
            """;

        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("test-uuid-123"));
    }
}