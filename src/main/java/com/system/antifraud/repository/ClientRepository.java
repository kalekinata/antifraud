package com.system.antifraud.repository;

import com.system.antifraud.models.db.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {
    @Query(value = "from Client where clid=:clid")
    Client findByClid(String clid);
}
