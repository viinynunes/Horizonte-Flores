package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ClienteDao;
import model.entities.Cliente;
import model.entities.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteDaoJDBC implements ClienteDao {

    private Connection conn;

    public ClienteDaoJDBC(Connection conn) {
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

            if (rows > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    cliente.setId(id);
                }
            } else
                System.out.println("Nenhuma linha afetada");

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }


    }

    @Override
    public void update(Cliente cliente) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE cliente " +
                    "set nome = ?, telefone = ?, telefone2 = ?, email = ?, cpf = ?, cnpj = ?, endereco_id = ?");

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
            st = conn.prepareStatement("Delete from cliente where id = ?");
            st.setInt(1, id);

            st.executeQuery();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Cliente findById(Integer id) {

        PreparedStatement st;
        ResultSet rs;

        try {
            st = conn.prepareStatement("select * from cliente inner join endereco" +
                    "on cliente.endereco_id = endereco.id " +
                    "where id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()){
                Endereco endereco = createEndereco(rs);

                return createCliente(rs, endereco);
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }

        return null;

    }

    @Override
    public List<Cliente> findByName(String nome) {

        List<Cliente> list = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        try {
            st = conn.prepareStatement("select * from cliente inner join endereco " +
                    "on cliente.endereco_id = endereco.id " +
                    "where cliente.nome = ?");

            st.setString(1, nome);

            rs = st.executeQuery();

            Map<Integer, Endereco> map = new HashMap<>();

            while (rs.next()){
                Endereco endereco = map.get(rs.getInt("endereco_id"));

                if (endereco == null){
                    endereco = createEndereco(rs);
                    map.put(rs.getInt("endereco_id"), endereco);
                }

                Cliente cliente = createCliente(rs, endereco);
                list.add(cliente);
                return list;
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Cliente> findAll() {

        List<Cliente> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("Select * from cliente inner join endereco " +
                    "on cliente.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Endereco> map = new HashMap<>();

            while (rs.next()) {
                Endereco endereco = map.get(rs.getInt("endereco_id"));

                if (endereco == null) {
                    endereco = createEndereco(rs);
                    map.put(rs.getInt("endereco_id"), endereco);
                }

                Cliente cliente = createCliente(rs, endereco);
                list.add(cliente);

            }

            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    private Endereco createEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("endereco_id"));
        endereco.setLogadouro(rs.getString("endereco.logadouro"));
        endereco.setNumero(rs.getString("endereco.numero"));
        endereco.setBairro(rs.getString("endereco.bairro"));
        endereco.setReferencia(rs.getString("endereco.referencia"));
        endereco.setCidade(rs.getString("endereco.cidade"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setPais(rs.getString("pais"));

        return endereco;
    }

    private Cliente createCliente(ResultSet rs, Endereco endereco) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setTelefone2(rs.getString("telefone2"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCnpj(rs.getString("cnpj"));
        cliente.setEndereco(endereco);

        return cliente;
    }

}
