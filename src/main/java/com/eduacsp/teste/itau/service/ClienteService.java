package com.eduacsp.teste.itau.service;

import com.eduacsp.teste.itau.model.Cliente;
import com.eduacsp.teste.itau.repository.ClienteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private static final Logger log = LogManager.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        log.debug("findAll");
        List<Cliente> listAll = new ArrayList<Cliente>();
        clienteRepository.findAll().iterator().forEachRemaining(listAll::add);
        return listAll;
    }

    public Cliente create(Cliente cliente) {
        log.debug("create");
        return clienteRepository.save(cliente);
    }

    public Cliente findByNumeroConta(Long numeroConta) {
        log.debug("findByNumeroConta");
        return clienteRepository.findByIdConta(numeroConta);
    }

    public void delete(Long numeroConta) {
        log.debug("delete");
        Cliente clienteDel = clienteRepository.findByIdConta(numeroConta);
        clienteRepository.delete(clienteDel);
    }


}
