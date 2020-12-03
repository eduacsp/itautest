package com.eduacsp.teste.itau.service;

import com.eduacsp.teste.itau.excep.ValidacaoException;
import com.eduacsp.teste.itau.model.Cliente;
import com.eduacsp.teste.itau.model.Movimentacao;
import com.eduacsp.teste.itau.repository.ClienteRepository;
import com.eduacsp.teste.itau.repository.MovimentacaoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MovimentacaoCliente {

    private static final Logger log = LogManager.getLogger(MovimentacaoCliente.class);

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    private static final BigDecimal VALOR_MAXIMO_TRANSF = new BigDecimal("1000.0");

    public Movimentacao transferir(Long numeroContaOrigem, Long numeroContaDestino, String valor) throws ValidacaoException {
        log.info("transferir: " + numeroContaOrigem + ",numeroContaDestino: " + numeroContaDestino + ",valor: " + valor);
        Cliente clienteOrigem = clienteRepository.findByIdConta(numeroContaOrigem);
        Cliente clienteDestino = clienteRepository.findByIdConta(numeroContaDestino);
        String validacao = validarTransferencia(clienteOrigem, clienteDestino, new BigDecimal(valor));
        boolean valida = (validacao == null ? true : false);
        Movimentacao movimentacao = salvarMovimentacao(valor, clienteOrigem, clienteDestino, valida);
        if (!valida) {
            throw new ValidacaoException(validacao);
        }
        alterarSaldos(clienteOrigem, clienteDestino, movimentacao);
        return movimentacao;
    }

    public List<Movimentacao> findByNumeroConta(Long numeroConta) {
        log.debug("findByNumeroConta:" + numeroConta);
        Cliente cliente = clienteRepository.findByIdConta(numeroConta);
        log.debug("findByNumeroConta - cliente:" + cliente.toString());
        return movimentacaoRepository.findByClienteOrigemOrClienteDestinoOrderByDataDesc(cliente);
    }


    public void delete(Long numeroConta) {
        log.debug("delete");
        Cliente clienteDel = clienteRepository.findByIdConta(numeroConta);
        List<Movimentacao> list = movimentacaoRepository.findByClienteOrigemOrClienteDestinoOrderByDataDesc(clienteDel);
        for (Movimentacao movimentacao : list) {
            movimentacaoRepository.delete(movimentacao);
        }
    }


    private void alterarSaldos(Cliente clienteOrigem, Cliente clienteDestino, Movimentacao movimentacao) {
        clienteOrigem.setSaldo(clienteOrigem.getSaldo().subtract(movimentacao.getValor()));
        clienteRepository.save(clienteOrigem);
        clienteDestino.setSaldo(clienteDestino.getSaldo().add(movimentacao.getValor()));
        clienteRepository.save(clienteDestino);
    }

    private Movimentacao salvarMovimentacao(String valor, Cliente clienteOrigem, Cliente clienteDestino, boolean sucesso) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setClienteOrigem(clienteOrigem);
        movimentacao.setClienteDestino(clienteDestino);
        movimentacao.setValor(new BigDecimal(valor));
        movimentacao.setData(new Date());
        movimentacao.setSucesso(sucesso);
        movimentacao = movimentacaoRepository.save(movimentacao);
        return movimentacao;
    }

    private String validarTransferencia(Cliente clienteOrigem, Cliente clienteDestino, BigDecimal valor) {
        String validaStr = null;
        boolean valida = false;
        if (valor == null) {
            validaStr = "Valor inválido! ";
            valida = true;
        }
        if (!valida && valor.compareTo(VALOR_MAXIMO_TRANSF) >= 0) {
            validaStr = "Valor máximo de transferência ultrapassado! ";
            valida = true;
        }
        if (!valida && clienteOrigem.getSaldo().compareTo(valor) < 0) {
            validaStr = "Conta de origem não possui recursos suficientes! ";
        }
        return validaStr;
    }



}
