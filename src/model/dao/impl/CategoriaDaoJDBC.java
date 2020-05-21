package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.CategoriaDao;
import model.entities.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoJDBC implements CategoriaDao {

    private Connection conn = null;

    public CategoriaDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Categoria categoria) {
        PreparedStatement st = null;

        try {

            st = conn.prepareStatement("insert into categoria (nome, abreviacao) values (?, ?)");

            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getAbreviacao());

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Categoria categoria) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("update categoria set nome = ?, abreviacao = ? where id = ?");

            st.setString(1, categoria.getNome());
            st.setString(2, categoria.getAbreviacao());
            st.setInt(3, categoria.getId());

            st.executeQuery();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("delete from categoria where id = ?");

            st.setInt(1, id);

            st.executeQuery();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public List<Categoria> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Categoria> list = new ArrayList<>();

        try {
            st = conn.prepareStatement("select * from categoria");

            rs = st.executeQuery();

            while (rs.next()){
                Categoria categoria = createCategoria(rs);
                list.add(categoria);
            }

            return list;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }
    }

    private Categoria createCategoria(ResultSet rs) throws SQLException{

        Categoria categoria = new Categoria();

        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setAbreviacao(rs.getString("abreviacao"));

        return categoria;
    }
}

