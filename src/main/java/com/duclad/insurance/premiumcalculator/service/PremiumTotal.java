package com.duclad.insurance.premiumcalculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class PremiumTotal {

    private BigDecimal amount = BigDecimal.ZERO;


    public void add(BigDecimal premium){
        amount = amount.add(premium).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount(){
        return amount;
    }

}
