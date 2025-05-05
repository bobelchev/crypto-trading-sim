package com.example.crypto.repository;

import com.example.crypto.model.Transaction;
import com.example.crypto.repository.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Returns all transactions of a specific user from the db
     * @param userId
     * @return list of transactions belonging to user
     */
    public List<Transaction> getAllTxForUser(long userId){
        String sql = "SELECT * FROM transactions WHERE user_id=?";
        return jdbcTemplate.query(sql,new Object[]{userId}, new TransactionMapper());
    }

    /**
     * Returns a single transaction accord. to the id from the db
     * @param txId
     * @return transaction with that id
     */
    public Transaction getSingleTx(long txId){
        String sql = "SELECT * FROM transactions WHERE id=?";
        return jdbcTemplate.queryForObject(sql,new Object[] {txId}, new TransactionMapper());
    }

    /**
     * Insert a new transaction into the db
     * @param transaction
     */
    public void insertTx(Transaction transaction){
        String sql = """
        INSERT INTO transactions (user_id, crypto_ticker, quantity, price, transaction_type, timestamp)
        VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                transaction.getUserId(),transaction.getCryptoTicker(),
                transaction.getQuantity(), transaction.getPrice(),
                transaction.getTransactionType().name(),transaction.getTimestamp());
    }
    /**
     * Deletes all transactions of a user from the DB
     * @param userId
     */
    public void deleteAllTxs(long userId){
    }
}
