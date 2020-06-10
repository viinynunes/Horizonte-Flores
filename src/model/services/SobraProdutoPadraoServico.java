package model.services;

import model.dao.DaoFactory;
import model.dao.SobraProdutoPadraoDao;
import model.entities.Fornecedor;
import model.entities.SobraProdutoPadrao;

import java.util.List;

public class SobraProdutoPadraoServico {

    private SobraProdutoPadraoDao dao = DaoFactory.createSobraPadraoDao();

    public void insertOrUpdate(SobraProdutoPadrao padrao){
        if (padrao.getId() == null){
            dao.insert(padrao);
        } else {
            dao.update(padrao);
        }
    }

    public List<SobraProdutoPadrao> findByFornecedor(Fornecedor fornecedor){
        return dao.findByFornecedor(fornecedor);
    }

    public List<SobraProdutoPadrao> findAll(){
        return dao.findAll();
    }
}
