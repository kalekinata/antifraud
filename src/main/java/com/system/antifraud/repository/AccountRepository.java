package com.system.antifraud.repository;

import com.system.antifraud.models.db.Accounts;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Accounts,String> {
    Optional<Accounts> findByClid(String clid);
}
