package model.dao;

import model.entities.Endereco;

import java.util.List;

public interface EnderecoDao {

    void insert (Endereco endereco);
    void update (Endereco endereco);
    void deleteById (Integer id);
    List<Endereco> findAll();
}
