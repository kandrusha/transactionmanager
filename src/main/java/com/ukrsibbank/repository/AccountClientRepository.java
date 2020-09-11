package com.ukrsibbank.repository;

import com.ukrsibbank.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountClientRepository extends CrudRepository<Client, Long> {

  Client findByInn(String inn);
}
