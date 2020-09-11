package com.ukrsibbank.repository;

import com.ukrsibbank.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionRepository extends CrudRepository<Transaction, Long> {
}
