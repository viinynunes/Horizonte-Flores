package model.services;

import model.entities.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteServico {

    public List<Cliente> findAll(){
        List<Cliente> list = new ArrayList<>();

        Cliente cliente = new Cliente();
        Cliente cliente1 = new Cliente();
        cliente.setId(1);
        cliente.setNome("Vinicius");

        cliente1.setId(2);
        cliente1.setNome("Maria");

        list.add(cliente);
        list.add(cliente1);

        return list;
    }
}
