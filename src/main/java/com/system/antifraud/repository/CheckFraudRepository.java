package com.system.antifraud.repository;

import com.system.antifraud.models.db.CheckFraud;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CheckFraudRepository extends CrudRepository<CheckFraud, String> {
    CheckFraud findByCheckid(String checkid);

    @Query(value = "from CheckFraud order by dadd desc")
    Iterable<CheckFraud> findAllSort();

    @Modifying
    @Transactional
    @Query("update CheckFraud set status_check=:status_check, description=:desc where checkid=:checkid")
    int setFixedCheckStatus(String checkid, String status_check, String desc);
}
