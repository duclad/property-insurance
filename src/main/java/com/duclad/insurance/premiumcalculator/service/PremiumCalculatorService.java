package com.duclad.insurance.premiumcalculator.service;

import com.duclad.insurance.premiumcalculator.model.Policy;
import com.duclad.insurance.premiumcalculator.model.PolicyObject;
import com.duclad.insurance.premiumcalculator.model.PolicySubObject;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PremiumCalculatorService {

    private final KieContainer kieContainer;
    private final Validator validator;

    public BigDecimal calculatePremium(Policy policy) {
        validatePolicy(policy);
        PremiumTotal premiumTotal = new PremiumTotal();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("premiumTotal", premiumTotal);
        policy.getPolicyObjects().stream().flatMap(policyObject -> policyObject.getSubObjects().stream()).forEach(kieSession::insert);
        kieSession.fireAllRules();
        kieSession.dispose();
        return premiumTotal.getAmount();
    }

    private void validatePolicy(Policy policy) {
        Set<ConstraintViolation<Policy>> policyViolations = validator.validate(policy);
        StringBuilder sb = new StringBuilder();
        if (!policyViolations.isEmpty()) {

            for (ConstraintViolation<Policy> constraintViolation : policyViolations) {
                sb.append(constraintViolation.getPropertyPath().toString()).append(":").append(constraintViolation.getMessage()).append("; ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb, policyViolations);
        }

        policy.getPolicyObjects().forEach(policyObject -> {
            Set<ConstraintViolation<PolicyObject>> policyObjectsViolations = validator.validate(policyObject);
            if (!policyObjectsViolations.isEmpty()) {
                for (ConstraintViolation<PolicyObject> constraintViolation : policyObjectsViolations) {
                    sb.append(constraintViolation.getPropertyPath().toString()).append(":").append(constraintViolation.getMessage()).append("; ");
                }
                throw new ConstraintViolationException("Error occurred: " + sb, policyObjectsViolations);
            }

            policyObject.getSubObjects().forEach(policySubObject -> {
                Set<ConstraintViolation<PolicySubObject>> policySubObjectsViolations = validator.validate(policySubObject);
                if (!policySubObjectsViolations.isEmpty()) {
                    for (ConstraintViolation<PolicySubObject> constraintViolation : policySubObjectsViolations) {
                        sb.append(constraintViolation.getPropertyPath().toString()).append(":").append(constraintViolation.getMessage()).append("; ");
                    }
                    throw new ConstraintViolationException("Error occurred: " + sb, policySubObjectsViolations);
                }
            });
        });
    }
}
