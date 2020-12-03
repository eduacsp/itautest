package com.eduacsp.teste.itau.controller;

import com.eduacsp.teste.itau.excep.ValidacaoException;
import com.eduacsp.teste.itau.model.Cliente;
import com.eduacsp.teste.itau.service.ClienteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    private static final Logger log = LogManager.getLogger(ClienteController.class);

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody Cliente cliente) {
        log.debug("create");
        Cliente created = null;
        try {
            created = clienteService.create(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
        if (created != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<List<Cliente>> findAll() {
        log.debug("findAll");
        List<Cliente> list = clienteService.findAll();
        if (list.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/{numeroConta}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Cliente> findByNumeroConta(@PathVariable Long numeroConta) {
        log.debug("findByNumeroConta: "+numeroConta);
        Cliente cliente = clienteService.findByNumeroConta(numeroConta);
        if (cliente!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
