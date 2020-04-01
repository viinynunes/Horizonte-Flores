package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ClienteDao;
import model.entities.Cliente;

import java.sql.*;
import java.util.List;

public class ClienteDaoJDBC implements ClienteDao {

    private Connection conn;

    public ClienteDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Cliente cliente) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("insert into cliente (nome, telefone, telefone2, email, cpf, cnpj, endereco_id) " +
                    "values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, cliente.getNome());
            st.setString(2, cliente.getTelefone());
            st.setString(3, cliente.getTelefone2());
            st.setString(4, cliente.getEmail());
            st.setString(5, cliente.getCpf());
            st.setString(6, cliente.getCnpj());
            st.setInt(7, cliente.getEndereco().getId());

            int rows = st.executeUpdate();

            if (rows > 0){
                rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt("id");
                    cliente.setId(id);
                }
            } else
                System.out.println("Nenhuma linha afetada");

        }catch (SQLException e) {
            throw new DBException(e.getMessage());
        }finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }


    }

    @Override
    public void update(Cliente cliente) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Cliente findById(Integer id) {
        return null;
    }

    @Override
    public List<Cliente> findByName(String nome) {
        return null;
    }

    @Override
    public List<Cliente> findAll() {
        return null;
    }
}
