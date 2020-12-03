package com.eduacsp.teste.itau.repository;

import com.eduacsp.teste.itau.model.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    @Query(value = "select cli from com.eduacsp.teste.itau.model.Cliente cli where cli.idConta=?1")
    Cliente findByIdConta(Long idConta);


}
