package org.henry.virtualaccountsystem.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.henry.virtualaccountsystem.dto.DefaultResponse;
import org.henry.virtualaccountsystem.dto.SignUpDTO;
import org.henry.virtualaccountsystem.dto.enums.Role;
import org.henry.virtualaccountsystem.entity.Customer;
import org.henry.virtualaccountsystem.repository.UserRepository;
import org.henry.virtualaccountsystem.service.utils.PasswordValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountFactory {

    private final UserRepository userRepository;
    private final PasswordValidation passwordValidation;
    private final PasswordEncoder passwordEncoder;

    public DefaultResponse createAccount(SignUpDTO request){
        return signUp(request);
    }

    private DefaultResponse signUp(SignUpDTO signUpRequest){
        DefaultResponse res = new DefaultResponse();
        try {
            Customer customer = new Customer();

            boolean userAlreadyExist = userRepository.findByEmail(signUpRequest.getEmail()).isPresent();
            if(userAlreadyExist){
                res.setStatusCode(500);
                res.setMessage("Email Already Taken");
                return res;
            }

            log.info("Checking password Strength");
            if(!passwordValidation.verifyPasswordStrength(signUpRequest.getPassword())){
                log.error("Password not strong enough");
                res.setStatusCode(500);
                res.setMessage("Password should contain at least 8 characters,numbers and a symbol");
                return res;
            }

            log.info("Creating User Account");
            customer.setFirstName(signUpRequest.getFirstName());
            customer.setLastName(signUpRequest.getLastName());
            customer.setEmail(signUpRequest.getEmail());
            customer.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            customer.setPhone(signUpRequest.getPhone());
            customer.setUsername(signUpRequest.getEmail());
            customer.setRole(Role.USER);
            customer.setIsSuspended(false);
            userRepository.save(customer);

            log.info("Customer created and saved");
            res.setStatusCode(200);
            res.setMessage("Successful Signup...");
            res.setData(customer);
        }catch (Exception e){
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }
}
