package com.system.antifraud.repository;

import com.system.antifraud.models.db.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
    @Query(value = "from Transaction order by dadd desc")
    Iterable<Transaction> findAllSort();

    @Modifying
    @Transactional
    @Query("update Transaction t set t.status=:status where t.trid=:trid")
    int setFixedTranStatus(String trid, String status);

    @Query(value = "from Transaction where trid=:trid")
    Transaction findByTrid(String trid);


}
