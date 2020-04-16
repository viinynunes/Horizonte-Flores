package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.EnderecoDao;
import model.entities.Endereco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EnderecoDaoJDBC implements EnderecoDao {

    private Connection conn;
    PreparedStatement st = null;

    public EnderecoDaoJDBC (Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Endereco endereco) {

        try {
            st = conn.prepareStatement("insert into endereco (logadouro, numero, bairro, referencia, cep, cidade, estado, pais) values (?, ?, ?, ?, ?, ?, ?, ?)");
            st.setString(1, endereco.getLogadouro());
            st.setString(2, endereco.getNumero());
            st.setString(3, endereco.getBairro());
            st.setString(4, endereco.getReferencia());
            st.setString(5, endereco.getCep());
            st.setString(6, endereco.getCidade());
            st.setString(7, endereco.getEstado());
            st.setString(8, endereco.getPais());

            st.executeUpdate();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Endereco endereco) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<Endereco> findAll() {
        return null;
    }
}
