package com.example.holding_service.holding_service.repository.mapper;

import com.example.holding_service.holding_service.model.CryptoHolding;
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
