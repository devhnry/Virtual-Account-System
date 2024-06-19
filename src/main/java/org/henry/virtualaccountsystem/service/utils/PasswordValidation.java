package org.henry.virtualaccountsystem.service.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.henry.virtualaccountsystem.repository.IdGenRepository;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordValidation {

    private final IdGenRepository idGenRepository;

    public boolean verifyPasswordStrength(String pass){
        return passwordValidation(pass);
    }
    public String generateReference(){
        return generateRef();
    }

    private static boolean passwordValidation(String password)
    {
        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();
        }
        else
            return false;
    }

    private String generateRef(){
        String uniqueValue = String.valueOf(idGenRepository.count());
        String generatedValue;
        log.info("Generating Ref String");
        try {
            String ref = RandomStringUtils.random(11 - uniqueValue.length(), "013456789ABCDEFGHIJKLMNOPQRSTUV");
            generatedValue = ref + uniqueValue;
            return generatedValue;
        } catch (Exception ex) {
            log.info("Couldn't generate transaction reference => {}", ex.getMessage());
            throw new IdentifierGenerationException("Unexpected error occurred");
        }
    }
}
