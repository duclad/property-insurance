package com.duclad.insurance.premiumcalculator.service;

import com.duclad.insurance.premiumcalculator.model.Policy;
import com.duclad.insurance.premiumcalculator.model.PolicyObject;
import com.duclad.insurance.premiumcalculator.model.PolicySubObject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PremiumCalculatorServiceTest {

    @Autowired
    private PremiumCalculatorService premiumCalculatorService;

    @Test
    public void acceptanceTest1(){
        Policy policy = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                                .subObjects(List.of(
                                        PolicySubObject.builder().riskType("FIRE").insuredAmount(new BigDecimal(100)).policySubObjectsName("tv").build(),
                                        PolicySubObject.builder().riskType("THEFT").insuredAmount(new BigDecimal(8)).policySubObjectsName("couch").build()
                                ))
                        .build())).build();
        assertEquals(0, new BigDecimal("2.28").compareTo(premiumCalculatorService.calculatePremium(policy)));
    }

    @Test
    public void acceptanceTest2(){
        Policy policy = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                        .subObjects(List.of(
                                PolicySubObject.builder().riskType("FIRE").insuredAmount(new BigDecimal(500)).policySubObjectsName("tv").build(),
                                PolicySubObject.builder().riskType("THEFT").insuredAmount(new BigDecimal("102.51")).policySubObjectsName("couch").build()
                        ))
                        .build())).build();
        BigDecimal result = premiumCalculatorService.calculatePremium(policy);
        System.out.println(result);
        assertEquals(0, new BigDecimal("17.13").compareTo(result));
    }

    @Test
    public void testValidation(){
        Policy policy1 =Policy.builder().build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy1));
        Policy policy2 = Policy.builder().policyNumber("1").build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy2));
        Policy policy3 = Policy.builder().policyNumber("1").policyStatus("APPROVED").build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy3));
        Policy policy4 = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().build())).build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy4));
        Policy policy5 = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house").build())).build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy5));
        Policy policy6 = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                        .subObjects(List.of(
                                PolicySubObject.builder().build()
                        ))
                        .build())).build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy6));
        Policy policy7 = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                        .subObjects(List.of(
                                PolicySubObject.builder().riskType("FIRE").build()
                        ))
                        .build())).build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy7));
        Policy policy8 = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                        .subObjects(List.of(
                                PolicySubObject.builder().riskType("FIRE").insuredAmount(new BigDecimal(500)).build()
                        ))
                        .build())).build();
        assertThrows(ConstraintViolationException.class, ()->premiumCalculatorService.calculatePremium(policy8));
        Policy policy = Policy.builder().policyNumber("1").policyStatus("APPROVED")
                .policyObjects(List.of(PolicyObject.builder().policyObjectName("house")
                        .subObjects(List.of(
                                PolicySubObject.builder().riskType("FIRE").insuredAmount(new BigDecimal(500)).policySubObjectsName("tv").build()
                        ))
                        .build())).build();
        assertNotNull(policy);
    }

}
