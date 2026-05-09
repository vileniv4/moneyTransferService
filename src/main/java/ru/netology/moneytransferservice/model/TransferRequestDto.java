package ru.netology.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotBlank
    private String cardFromNumber;

    @NotBlank
    private String cardFromValidTill;

    @NotBlank
    private String cardFromCVV;

    @NotBlank
    private String cardToNumber;

    @NotNull
    private Amount amount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        @Positive
        private Integer value;
        @NotBlank
        private String currency;
    }
}