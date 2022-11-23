package com.ns.bankingapp.util;

import org.springframework.security.core.parameters.P;

import java.util.Random;

public class IBANUtil {

    public static String generateIBAN(){
        Random random = new Random();
        String IBAN = "AL";

        for (int i = 0; i < 26; i++){
            int digit = random.nextInt(9);
            IBAN = IBAN.concat(String.valueOf(digit));
        }
        return IBAN;
    }
}
