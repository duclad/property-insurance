package com.duclad.insurance.premiumcalculator.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PolicySubObject {

    @NotBlank
    private String policySubObjectsName;

    @NotNull
    private BigDecimal insuredAmount;

    @NotBlank
    private String riskType;
}
