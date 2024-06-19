package org.henry.virtualaccountsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.henry.virtualaccountsystem.dto.*;
import org.henry.virtualaccountsystem.entity.Customer;
import org.henry.virtualaccountsystem.entity.OTP;
import org.henry.virtualaccountsystem.repository.OTPRepository;
import org.henry.virtualaccountsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Supplier;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPRepository otpRepository;


    private Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((Customer) authentication.getPrincipal()).getUserId();
    }

    private Customer getDetails(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("User with id " + id + "does not exist"));
    }

    Supplier<Customer> getCurrentUser = () -> { Long id = getUserId(); Customer customer = getDetails(id);
        return customer;
    };

    public OTP generateOTP(){
        OTP otp = new OTP();
        Customer currentCustomer = getCurrentUser.get();

        otp.setOtpCode(new Random().nextLong(100000L));
        otp.setCustomer(currentCustomer);
        otp.setExpired(false);
        otp.setExpiresIn("4 Minutes");

        long expirationTime = 240000;

        long currentTime = System.currentTimeMillis();
        otp.setGeneratedTime(currentTime);

        long expirationTimestamp = currentTime + expirationTime;
        otp.setExpirationTime(expirationTimestamp);

        otpRepository.save(otp);
        return otp;
    }


    private String validateOTP(Long otpCode) {
        String message;
        Customer customer = getCurrentUser.get();

        log.info("Finding User by OtpCode");
        Optional<OTP> otpOptional = otpRepository.findByCustomerAndOtpCode(customer, otpCode);
        if (otpOptional.isPresent()) {
            log.info("User was found with OTPCode");
            OTP otp = otpOptional.get();
            long currentTime = System.currentTimeMillis();
            log.info("Checking OTP expiration time");
            if (otp.getExpirationTime() - currentTime < 0 || otp.getExpired()) {
                message = "valid";
            } else {
                message = "expired";
            }
        } else {
            message = "invalid";
        }

        return message;
    }

    @Transactional
    public DefaultResponse updateDetails(UpdateInfoDTO req){
        DefaultResponse response = new DefaultResponse();
        Customer customer = getCurrentUser.get();

        Optional<OTP> otpOptional = otpRepository.findByCustomerAndOtpCode(customer, req.getOtpCode());
        var otp = otpOptional.orElseThrow();


        String otpMessage = validateOTP(req.getOtpCode());
        if(otpMessage.equals("invalid")){
            response.setStatusCode(500);
            response.setMessage("Invalid OTP Code");

            return response;
        }else if(otpMessage.equals("expired")){
            response.setMessage("Expired OTP Code");
            response.setStatusCode(500);
            return response;
        }

        Optional<Customer> usersOptional =
                userRepository.findByEmail(req.getEmail());

        if (usersOptional.isPresent()){
            response.setStatusCode(500);
            response.setMessage("Email Already Taken");
            return response;
        }

        customer.setFirstName(req.getFirstName() == null ? customer.getFirstName() : req.getFirstName());
        customer.setLastName(req.getLastName() == null ? customer.getLastName() : req.getLastName());
        customer.setEmail(req.getEmail() == null ? customer.getEmail() : req.getEmail());
        customer.setPhone(req.getPhoneNumber() == null ? customer.getPhone() : req.getPhoneNumber());

        response.setStatusCode(200);
        response.setMessage("Successfully Updated");
        otp.setExpired(true);

        return response;
    }

    private DefaultResponse validateOTP(String otpMessage, DefaultResponse res) {
        if(otpMessage.equals("invalid")){
            res.setStatusCode(500);
            res.setMessage("Invalid OTP Credentials");
        }else if(otpMessage.equals("expired")){
            res.setStatusCode(500);
            res.setMessage("Expired OTP");
        }
        return res;
    }

    @Transactional
    public DefaultResponse resetPassword(PasswordResetDTO pass){
        DefaultResponse res = new DefaultResponse();
        Customer customer = getCurrentUser.get();

        Optional<OTP> otpOptional = otpRepository.findByCustomerAndOtpCode(customer, pass.getOtp());

        var otp = otpOptional.orElseThrow();
        log.info("Getting OTP Message");
        String otpMessage = validateOTP(pass.getOtp());

        log.info("Validating OTP");
        validateOTP(otpMessage, res);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String password = ((Customer) authentication.getPrincipal()).getPassword();

        if(passwordEncoder.matches(pass.getCurrentPassword(), password)){
            customer.setPassword(passwordEncoder.encode(pass.getNewPassword()));
            userRepository.save(customer);
            res.setStatusCode(200);
            res.setMessage("Successfully updated Password");
            otp.setExpired(true);
        }else{
            res.setStatusCode(500);
            res.setMessage("The current password is invalid");
            return res;
        }
        return res;
    }
}
