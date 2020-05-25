package model.dao;

import model.entities.Fornecedor;
import model.entities.Sobra;

import java.sql.Date;
import java.util.List;

public interface SobraDao {
    void insert(Sobra sobra);
    void update(Sobra sobra);
    void deleteById(Integer id);
    List<Sobra> findAll();
    List<Sobra> findByData(Date data, Date data2);
    List<Sobra> findByFornecedor(Fornecedor fornecedor);
}