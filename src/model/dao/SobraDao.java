package model.dao;

import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Sobra;

import java.sql.Date;
import java.util.List;

public interface SobraDao {
    void insert(Sobra sobra);
    void update(Sobra sobra);
    void deleteById(Integer id);
    void deleteByDateAndProdutoId(Date iniDate, Date endDate, Integer id);
    List<Sobra> findAll();
    List<Sobra> findByData(Date data, Date data2);
    List<Sobra> findByFornecedor(Fornecedor fornecedor, Date iniDate, Date endDate);
    List<Sobra> findByEstabelecimento(Estabelecimento estabelecimento, Date iniDate, Date endDate);
}