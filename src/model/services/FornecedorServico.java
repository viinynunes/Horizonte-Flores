package model.services;

import model.dao.DaoFactory;
import model.dao.FornecedorDao;
import model.entities.Fornecedor;

import java.util.List;

public class FornecedorServico {

    FornecedorDao dao = DaoFactory.createFornecedorDao();

    public List<Fornecedor> findAll(){
        return dao.findAll();
    }
}
