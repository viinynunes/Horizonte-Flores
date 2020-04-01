package model.dao.impl;

import model.dao.ProdutoDao;
import model.entities.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProdutoDaoJDBC implements ProdutoDao {

    private Connection conn;

    public ProdutoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Produto produto) {

    }

    @Override
    public void update(Produto produto) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Produto findById(Integer id) {
        return null;
    }

    @Override
    public List<Produto> findByName(String nome) {
        return null;
    }

    @Override
    public List<Produto> findAll() {
        return null;
    }
}
