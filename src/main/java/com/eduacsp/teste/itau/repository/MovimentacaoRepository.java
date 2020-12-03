package com.eduacsp.teste.itau.repository;

import com.eduacsp.teste.itau.model.Cliente;
import com.eduacsp.teste.itau.model.Movimentacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends CrudRepository<Movimentacao, Long> {

    @Query(value = "select mov from com.eduacsp.teste.itau.model.Movimentacao mov where mov.clienteOrigem=:cliente or mov.clienteDestino=:cliente order by mov.data desc")
    List<Movimentacao> findByClienteOrigemOrClienteDestinoOrderByDataDesc(@Param("cliente") Cliente cliente);

}
