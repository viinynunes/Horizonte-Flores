package model.services;

import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.entities.Cliente;
import model.entities.Endereco;

import java.util.ArrayList;
import java.util.List;

public class ClienteServico {

    private ClienteDao dao = DaoFactory.createClienteDao();

    public List<Cliente> findAll(){

        List<Cliente> list = dao.findAll();

        return list;
    }
}
