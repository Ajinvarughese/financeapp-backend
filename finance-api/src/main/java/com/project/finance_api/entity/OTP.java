package com.project.finance_api.entity;

import com.project.finance_api.component.EntityDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTP extends EntityDetails {
    private String otp;
    private String email;
}