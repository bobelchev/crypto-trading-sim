package com.example.user_service.user_service.model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * POJO representng the user and its balance
 */
@Data
public class User {
    //made it for multiple users
    private long id;
    private String username;
    private String email;
    private String password;
    private BigDecimal balance;
}
