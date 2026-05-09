package ru.netology.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRequestDto {

    @NotBlank
    private String operationId;

    @NotBlank
    private String code;
}