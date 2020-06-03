package model.dao;

import model.entities.Cliente;
import model.entities.Fornecedor;

import java.sql.Date;
import java.util.List;

public interface FornecedorDao {
    void insert (Fornecedor fornecedor);
    void update (Fornecedor fornecedor);
    void deleteById (Integer id);
    List<Fornecedor> findAll();
    List<Fornecedor> findByData(Date iniDate, Date endDate);
    List<Fornecedor> findByClienteAndData(Cliente cliente, Date date);
}
