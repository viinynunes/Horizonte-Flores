package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Categoria;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Produto;

public class ProdutoServico {

    private ProdutoDao dao = DaoFactory.createProdutoDao();

    public List<Produto> findAll() {

        List<Produto> list = dao.findAll();

        return list;
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
