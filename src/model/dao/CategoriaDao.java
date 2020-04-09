package model.dao;

import model.entities.Categoria;

import java.util.List;

public interface CategoriaDao {

    void insert(Categoria categoria);
    void update(Categoria categoria);
    void deleteById(Integer id);
    List<Categoria> findAll();
}
