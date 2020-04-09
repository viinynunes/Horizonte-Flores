package model.dao;

import model.entities.Fornecedor;

import java.util.List;

public interface FornecedorDao {
    void insert (Fornecedor fornecedor);
    void update (Fornecedor fornecedor);
    void deleteById (Integer id);
    List<Fornecedor> findAll();
}
