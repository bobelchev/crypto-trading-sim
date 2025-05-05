package com.example.crypto.repository.mapper;

import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
//https://stackoverflow.com/questions/15118630/some-doubts-about-rowmapper-use-in-jdbc-in-a-spring-framework-application
//https://stackoverflow.com/questions/40153567/getting-a-single-object-of-model-class-in-spring-framework
//https://www.baeldung.com/spring-jdbc-jdbctemplate
/**
 * Maps the result from the query to a Transaction object
 */
public class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transaction(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("crypto_ticker"),
                    rs.getBigDecimal("quantity"),
                    rs.getBigDecimal("price"),
                    rs.getTimestamp("timestamp").toLocalDateTime(),
                    TransactionType.valueOf(rs.getString("transaction_type"))
            );
        }
}
