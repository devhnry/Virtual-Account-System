package org.henry.virtualaccountsystem.repository;

import org.henry.virtualaccountsystem.entity.Customer;
import org.henry.virtualaccountsystem.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findById(Long id);
    Optional<OTP> findByCustomerAndOtpCode(Customer customer, Long otpCode );
}
