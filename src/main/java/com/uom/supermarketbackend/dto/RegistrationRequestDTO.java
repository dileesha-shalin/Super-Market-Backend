package com.uom.supermarketbackend.dto;

import com.uom.supermarketbackend.enums.RoleType;
import lombok.Data;

@Data
public class RegistrationRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleType role;
}
