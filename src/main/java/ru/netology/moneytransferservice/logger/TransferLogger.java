package ru.netology.moneytransferservice.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.netology.moneytransferservice.model.TransferRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class TransferLogger {

    private static final String LOG_FILE = "logs/transfers.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logTransfer(TransferRequest request, String operationId,
                            String status, double commission, String result) {
        int amountValue = request.getAmount() != null ? request.getAmount().getValue() : 0;
        String amountCurrency = request.getAmount() != null ? request.getAmount().getCurrency() : "RUB";

        String logEntry = String.format("%s;%s;%s;%s;%d;%.2f;%s;%s%n",
                LocalDateTime.now().format(FORMATTER),
                request.getCardFromNumber(),
                request.getCardToNumber(),
                operationId,
                amountValue,
                commission,
                status,
                result != null ? result : "OK"
        );

        try {
            Path logPath = Paths.get(LOG_FILE);
            Files.createDirectories(logPath.getParent());
            Files.writeString(logPath, logEntry,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            log.info("Transfer logged: {}", operationId);
        } catch (IOException e) {
            log.error("Failed to write to log file: {}", LOG_FILE, e);
        }
    }
}