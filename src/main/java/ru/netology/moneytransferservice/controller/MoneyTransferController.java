package ru.netology.moneytransferservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.logger.TransferLogger;
import ru.netology.moneytransferservice.model.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MoneyTransferController {

    private final TransferLogger transferLogger;

    /**
     * Инициация перевода денег с карты на карту
     * POST /transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransferRequestDto request) {
        try {
            String operationId = UUID.randomUUID().toString();

            // Преобразуем DTO в внутреннюю модель для логирования
            // Обратите внимание: CVV не логируем в открытом виде (безопасность)
            TransferRequest modelRequest = new TransferRequest(
                    request.getCardFromNumber(),
                    request.getCardFromValidTill(),
                    "***", // Маскируем CVV в логах
                    request.getCardToNumber(),
                    new TransferRequest.Amount(
                            request.getAmount().getValue(),
                            request.getAmount().getCurrency()
                    )
            );

            String logMessage = String.format(
                    "FROM: %s (Valid: %s) TO: %s AMOUNT: %d %s",
                    request.getCardFromNumber(),
                    request.getCardFromValidTill(),
                    request.getCardToNumber(),
                    request.getAmount().getValue(),
                    request.getAmount().getCurrency()
            );

            transferLogger.logTransfer(modelRequest, operationId, "PENDING", 0.0, logMessage);

            return ResponseEntity.ok(new TransferResponseDto(operationId));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponseDto("Error transfer", 500));
        }
    }

    /**
     * Подтверждение операции кодом из СМС
     * POST /confirmOperation
     */
    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirmOperation(@Valid @RequestBody ConfirmRequestDto request) {
        try {
            String logMessage = "Confirm operation with code: " + request.getCode();

            // Для подтверждения у нас нет данных карт, создаём заглушку для лога
            TransferRequest modelRequest = new TransferRequest(
                    "****", "****", "****", "****",
                    new TransferRequest.Amount(0, "RUB")
            );

            transferLogger.logTransfer(modelRequest, request.getOperationId(), "CONFIRMED", 0.0, logMessage);

            return ResponseEntity.ok(new TransferResponseDto(request.getOperationId()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponseDto("Error confirmation", 500));
        }
    }
}