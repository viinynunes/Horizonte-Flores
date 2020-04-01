package model.dao;

import model.entities.Produto;

import java.util.List;

public interface ProdutoDao {

    void insert(Produto produto);
    void update(Produto produto);
    void deleteById(Integer id);
    Produto findById(Integer id);
    List<Produto> findByName(String nome);
    List<Produto> findAll();
}
