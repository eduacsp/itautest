package com.eduacsp.teste.itau.app;

import com.eduacsp.teste.itau.controller.ClienteController;
import com.eduacsp.teste.itau.controller.MovimentacaoController;
import com.eduacsp.teste.itau.model.Cliente;
import com.eduacsp.teste.itau.model.Movimentacao;
import com.eduacsp.teste.itau.service.ClienteService;
import com.eduacsp.teste.itau.service.MovimentacaoCliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {com.eduacsp.teste.itau.app.ItauApiApplication.class})
@TestPropertySource(locations = "/application-test.properties")
public class ItauapiApplicationTests {

    private static final Logger log = LogManager.getLogger(ItauapiApplicationTests.class);

    @Autowired
    private ClienteController clienteController;

    @Autowired
    private MovimentacaoController movimentacaoController;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MovimentacaoCliente movimentacaoCliente;

    @Test
    void cadastrarClienteOK() {
        log.info("cadastrarClienteOK");
        apagarTodosClientes();
        ResponseEntity<String> clienteResp = criarCliente(1L,new BigDecimal("10.5"));
        assertEquals(HttpStatus.CREATED, clienteResp.getStatusCode());
    }

    @Test
    void cadastrarClienteComErroDeContaJaExistente() {
        log.info("cadastrarClienteComErroDeContaJaExistente");
        apagarTodosClientes();
        ResponseEntity<String> clienteResp = criarCliente(99L,new BigDecimal("10.5"));
        assertEquals(HttpStatus.CREATED, clienteResp.getStatusCode());
        ResponseEntity<String> clienteResp2 = criarCliente(99L,new BigDecimal("11.5"));
        assertEquals(HttpStatus.BAD_REQUEST, clienteResp2.getStatusCode());
    }

    @Test
    void listarClientesCadastradosOk() {
        log.info("listarClientesCadastradosOk");
        apagarTodosClientes();
        ResponseEntity<String> clienteResp5 = criarCliente(5L,new BigDecimal("10.5"));
        ResponseEntity<String> clienteResp6 = criarCliente(6L,new BigDecimal("11.5"));
        ResponseEntity<List<Cliente>> listar2 = clienteController.findAll();
        assertEquals(2, listar2.getBody().size());
    }

    @Test
    void buscarClientePorNumeroDeContaOK() {
        log.info("buscarClientePorNumeroDeContaOK");
        apagarTodosClientes();
        ResponseEntity<String> clienteResp5 = criarCliente(1L,new BigDecimal("100.5"));
        ResponseEntity<Cliente> cliente = clienteController.findByNumeroConta(1L);
        assertNotNull(cliente.getBody());
        assertEquals(1L, cliente.getBody().getIdConta());
    }

    @Test
    void efetuarTransferenciaComSaldoOK() {
        log.info("efetuarTransferenciaComSaldoOK");
        apagarTodosClientes();
        criarCliente(10L,new BigDecimal("100.5"));
        criarCliente(20L,new BigDecimal("150.5"));
        ResponseEntity<String> mov = movimentacaoController.create(10L, 20L, "100.5");
        assertEquals(HttpStatus.CREATED, mov.getStatusCode());
    }

    @Test
    void efetuarTransferenciaComSaldoInsuficiente() {
        log.info("efetuarTransferenciaComSaldoInsuficiente");
        apagarTodosClientes();
        criarCliente(10L,new BigDecimal("100.5"));
        criarCliente(20L,new BigDecimal("150.5"));
        ResponseEntity<String> mov = movimentacaoController.create(10L, 20L, "101.5");
        assertEquals(HttpStatus.BAD_REQUEST, mov.getStatusCode());
    }

    @Test
    void efetuarTransferenciaComSaldoOKEVerificandoSeDestinoEstaOK() {
        log.info("efetuarTransferenciaComSaldoOKEVerificandoSeDestinoEstaOK");
        apagarTodosClientes();
        criarCliente(10L,new BigDecimal("100.5"));
        criarCliente(20L,new BigDecimal("150.5"));
        ResponseEntity<String> mov = movimentacaoController.create(10L, 20L, "100.5");
        assertEquals(HttpStatus.CREATED, mov.getStatusCode());
        ResponseEntity<Cliente> destino = clienteController.findByNumeroConta(20L);
        assertEquals(new BigDecimal("251.00"), destino.getBody().getSaldo());
    }

    @Test
    void buscarTransferencias() {
        log.info("buscarTransferencias");
        apagarTodosClientes();
        criarCliente(10L,new BigDecimal("100.5"));
        criarCliente(20L,new BigDecimal("150.5"));
        movimentacaoController.create(10L, 20L, "50.5");
        movimentacaoController.create(10L, 20L, "100.5");
        movimentacaoController.create(20L, 10L, "80.5");
        ResponseEntity<List<Movimentacao>> listMov = movimentacaoController.findByNumeroConta(10L);
        assertEquals(3, listMov.getBody().size());
    }


    private void apagarTodosClientes() {
        ResponseEntity<List<Cliente>> listar = clienteController.findAll();
        for (Cliente cliente : listar.getBody()) {
            movimentacaoCliente.delete(cliente.getIdConta());
            clienteService.delete(cliente.getIdConta());
        }
    }

    private ResponseEntity<String> criarCliente(Long idConta, BigDecimal saldo) {
        log.debug("criarCliente");
        Cliente cliente = new Cliente();
        cliente.setIdConta(idConta);
        cliente.setNome("Teste");
        cliente.setSaldo(saldo);
        return clienteController.create(cliente);
    }
}
