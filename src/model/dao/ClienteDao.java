package model.dao;

import model.entities.Cliente;

import java.sql.Date;
import java.util.List;

public interface ClienteDao {

    void insert (Cliente cliente);
    void update (Cliente cliente);
    void deleteById (Integer id);
    Cliente findById(Integer id);
    List<Cliente> findByName(String nome);
    List<Cliente> findByData(Date date);
    List<Cliente> findAll();
}
