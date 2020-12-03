package com.eduacsp.teste.itau.controller;

import com.eduacsp.teste.itau.excep.ValidacaoException;
import com.eduacsp.teste.itau.model.Movimentacao;
import com.eduacsp.teste.itau.service.MovimentacaoCliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoCliente movimentacaoCliente;

    private static final Logger log = LogManager.getLogger(ClienteController.class);

    @PostMapping("/{origem}/{destino}/{valor}")
    public ResponseEntity<String> create(@PathVariable Long origem,
                                         @PathVariable Long destino,
                                         @PathVariable String valor) {
        log.debug("create");
        Movimentacao created = null;
        try {
            created = movimentacaoCliente.transferir(origem,destino,valor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
        if (created != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/{numeroConta}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<List<Movimentacao>> findByNumeroConta(@PathVariable Long numeroConta) {
        log.debug("findByNumeroConta: "+numeroConta);
        List<Movimentacao> list = movimentacaoCliente.findByNumeroConta(numeroConta);
        if (list.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
