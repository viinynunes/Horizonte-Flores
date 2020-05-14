package model.dao;

import model.entities.Estabelecimento;

import java.util.List;

public interface EstabelecimentoDao {

    void insert(Estabelecimento estabelecimento);
    void update(Estabelecimento estabelecimento);
    void deleteById(Integer id);
    List<Estabelecimento> findAll();
    Estabelecimento findById(Integer id);
}
