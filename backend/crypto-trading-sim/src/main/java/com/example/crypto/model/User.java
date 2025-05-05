package com.example.crypto.model;
import lombok.Data;

import java.math.BigDecimal;
/**
 * POJO representng the user and its balance
 */
@Data
public class User {
    //made it for multiple users
    private long id;
    private BigDecimal balance;

}
