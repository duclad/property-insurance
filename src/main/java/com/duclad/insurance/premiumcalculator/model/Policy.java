package com.duclad.insurance.premiumcalculator.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Policy {
    @NotEmpty
    List<PolicyObject> policyObjects;
    @NotBlank
    private String policyNumber;
    @NotBlank
    private String policyStatus;

}
