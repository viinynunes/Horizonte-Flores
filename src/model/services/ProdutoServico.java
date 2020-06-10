package model.services;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Fornecedor;
import model.entities.Produto;

import java.util.List;

public class ProdutoServico {

    private ProdutoDao dao = DaoFactory.createProdutoDao();

    public List<Produto> findAll() {

        List<Produto> list = dao.findAll();

        return list;
    }

    public List<Produto> findByFornecedor(Fornecedor fornecedor){
        return dao.findByFornecedor(fornecedor);
    }

    public void saveOrUpdate(Produto produto){
        if (produto.getId() == null){
            dao.insert(produto);
        } else {
            dao.update(produto);
        }
    }

    public void deleteById(Integer id) {
        dao.deleteById(id);
    }
}
