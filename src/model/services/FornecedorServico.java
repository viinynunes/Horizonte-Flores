package model.services;

import model.dao.DaoFactory;
import model.dao.FornecedorDao;
import model.entities.Fornecedor;

import java.util.List;

public class FornecedorServico {

    private FornecedorDao dao = DaoFactory.createFornecedorDao();

    public List<Fornecedor> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Fornecedor fornecedor){
        if (fornecedor.getId() == null){
            dao.insert(fornecedor);
        } else {
            dao.update(fornecedor);
        }
    }
}
