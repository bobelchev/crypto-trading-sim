package com.example.crypto.repository.mapper;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoHoldingMapper implements RowMapper<CryptoHolding> {
    @Override
    public CryptoHolding mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CryptoHolding(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("crypto_ticker"),
                rs.getBigDecimal("quantity"),
                rs.getBigDecimal("average_price"));
    }
}
