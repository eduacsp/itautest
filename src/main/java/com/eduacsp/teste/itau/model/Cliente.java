package com.eduacsp.teste.itau.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "cliente",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"id_conta"},
                        name = "uk_id_conta"
                )
        }
)
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_cliente", updatable = false, nullable = false)
    private UUID idCliente;

    @Column(name = "id_conta", updatable = false, unique = true, nullable = false)
    private Long idConta;

    @Column
    private String nome;

    @Column
    private BigDecimal saldo;

    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", idConta=" + idConta +
                ", nome='" + nome + '\'' +
                ", saldo=" + saldo +
                '}';
    }


}
