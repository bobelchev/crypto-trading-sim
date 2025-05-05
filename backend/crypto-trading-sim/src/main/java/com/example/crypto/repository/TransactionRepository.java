package com.example.crypto.repository;

import com.example.crypto.model.Transaction;
import com.example.crypto.repository.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
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
        List<Transaction> txs = jdbcTemplate.query(sql,new Object[]{userId}, new TransactionMapper());
        return txs;
    }

    /**
     * Returns a single transaction accord. to the id from the db
     * @param txId
     * @return transaction with that id
     */
    public Transaction getSingleTx(long txId){
        String sql = "SELECT * FROM transactions WHERE id=?";
        Transaction transaction = jdbcTemplate.queryForObject(sql,new Object[] {txId}, new TransactionMapper());
        return transaction;
    }

    /**
     * Insert a new transaction into the db
     *  TODO: implement SQL query
     * @param transaction
     * @return
     */
    public long insertTx(Transaction transaction){

        return 1L;
    }
}
