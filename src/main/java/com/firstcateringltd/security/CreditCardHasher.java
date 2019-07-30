package com.firstcateringltd.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CreditCardHasher {

    public String hashCreditCard(String plainCreditCardNumbers) {
        String hashedCreditCard = BCrypt.hashpw(plainCreditCardNumbers, BCrypt.gensalt(9));
        return hashedCreditCard;
    }
}
