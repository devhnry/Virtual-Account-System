package org.henry.virtualaccountsystem.controller;

import lombok.RequiredArgsConstructor;
import org.henry.virtualaccountsystem.dto.*;
import org.henry.virtualaccountsystem.entity.OTP;
import org.henry.virtualaccountsystem.service.AccountService;
import org.henry.virtualaccountsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;

    @GetMapping("/get-vba-details")
    public KoraResponse getVirtualAccountDetails(@RequestBody AccountRequestDTO requestDTO){
        return accountService.getVirtualAccountDetails(requestDTO);
    }

    @PostMapping("/create-virtual-account")
    public KoraResponse createVirtualAccount(@RequestBody AccountRequestDTO request){
        return accountService.createVirtualAccount(request);
    }

    @PostMapping("/credit")
    public KoraResponse creditAccount(@RequestBody FundAccountDTO request){
        return accountService.creditVirtualResponse(request);
    }

    @GetMapping("/transactions")
    public TransactionResponseDTO viewTransactions(@RequestBody FundAccountDTO request){
        return accountService.viewTransactions(request);
    }

    @PatchMapping("/updateProfile")
    public DefaultResponse updateInformation(@RequestBody UpdateInfoDTO req){
        return userService.updateDetails(req);
    }

    @PutMapping("/resetPassword")
    public DefaultResponse resetPassword(@RequestBody PasswordResetDTO pass){
        return userService.resetPassword(pass);
    }

    @GetMapping("/generateOtp")
    public OTP generateOTP(){
        return userService.generateOTP();
    }

}
