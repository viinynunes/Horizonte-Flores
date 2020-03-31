package model.services;

import model.entities.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteServico {

    public List<Cliente> findAll(){
        List<Cliente> list = new ArrayList<>();

        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Vinicius");

        list.add(cliente);

        return list;
    }
}
