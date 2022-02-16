package com.duclad.insurance.premiumcalculator.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PolicyObject {
    @NotBlank
    private String policyObjectName;
    @NotEmpty
    List<PolicySubObject> subObjects;
}
