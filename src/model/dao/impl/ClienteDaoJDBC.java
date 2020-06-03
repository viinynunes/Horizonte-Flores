package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ClienteDao;
import model.entities.Cliente;
import model.entities.Endereco;
import model.util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteDaoJDBC implements ClienteDao {

    private final Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cliente cliente) {

        PreparedStatement st = null;

        try {

            if (cliente.getEndereco() == null) {
                st = conn.prepareStatement("insert into cliente (nome, telefone, telefone2, email, cpf, cnpj, endereco_id) " +
                        "values (?, ?, ?, ?, ?, ?, null)", Statement.RETURN_GENERATED_KEYS);

                st.setString(1, cliente.getNome());
                st.setString(2, cliente.getTelefone());
                st.setString(3, cliente.getTelefone2());
                st.setString(4, cliente.getEmail());
                st.setString(5, cliente.getCpf());
                st.setString(6, cliente.getCnpj());

            } else {
                st = conn.prepareStatement("insert into cliente (nome, telefone, telefone2, email, cpf, cnpj, endereco_id) " +
                        "values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                st.setString(1, cliente.getNome());
                st.setString(2, cliente.getTelefone());
                st.setString(3, cliente.getTelefone2());
                st.setString(4, cliente.getEmail());
                st.setString(5, cliente.getCpf());
                st.setString(6, cliente.getCnpj());
                st.setInt(7, cliente.getEndereco().getId());
            }

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }


    }

    @Override
    public void update(Cliente cliente) {
        PreparedStatement st = null;

        try {
            if (cliente.getEndereco() == null) {
                st = conn.prepareStatement("UPDATE cliente " +
                        "set nome = ?, telefone = ?, telefone2 = ?, email = ?, cpf = ?, cnpj = ?, endereco_id = null " +
                        "where id = ?");

                st.setString(1, cliente.getNome());
                st.setString(2, cliente.getTelefone());
                st.setString(3, cliente.getTelefone2());
                st.setString(4, cliente.getEmail());
                st.setString(5, cliente.getCpf());
                st.setString(6, cliente.getCnpj());
                st.setInt(7, cliente.getId());

            } else {

                st = conn.prepareStatement("UPDATE cliente " +
                        "set nome = ?, telefone = ?, telefone2 = ?, email = ?, cpf = ?, cnpj = ?, endereco_id = ? " +
                        "where id = ?");

                st.setString(1, cliente.getNome());
                st.setString(2, cliente.getTelefone());
                st.setString(3, cliente.getTelefone2());
                st.setString(4, cliente.getEmail());
                st.setString(5, cliente.getCpf());
                st.setString(6, cliente.getCnpj());
                st.setInt(7, cliente.getEndereco().getId());
                st.setInt(8, cliente.getId());

            }
            st.executeUpdate();

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

            st.executeUpdate();
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

            if (rs.next()) {
                Endereco endereco = Utils.createEndereco(rs);

                return Utils.createCliente(rs, endereco);
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

            while (rs.next()) {
                Endereco endereco = map.get(rs.getInt("endereco_id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    map.put(rs.getInt("endereco_id"), endereco);
                }

                Cliente cliente = Utils.createCliente(rs, endereco);
                list.add(cliente);
            }
            return list;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<Cliente> findByData(Date iniDate) {
        List<Cliente> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Cliente cliente;
        Endereco endereco;

        try {
            st = conn.prepareStatement("select * from cliente inner join endereco on cliente.endereco_id = endereco.id " +
                    "inner join pedido on pedido.cliente_id = cliente.id " +
                    "inner join itens_do_pedido on itens_do_pedido.pedido_id = pedido.id " +
                    "where pedido.data = ? " +
                    "group by cliente.nome " +
                    "order by pedido.id desc");

            st.setDate(1, iniDate);
            rs = st.executeQuery();

            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                cliente = clienteMap.get(rs.getInt("id"));
                endereco = enderecoMap.get(rs.getInt("endereco_id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco_id"), endereco);
                }
                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("id"), cliente);
                }
                list.add(cliente);
            }

            st = conn.prepareStatement("select * from cliente  " +
                    "inner join pedido on pedido.cliente_id = cliente.id " +
                    "inner join itens_do_pedido on itens_do_pedido.pedido_id = pedido.id " +
                    "where pedido.data = ? " +
                    "group by cliente.nome " +
                    "order by pedido.id desc");

            st.setDate(1, iniDate);
            rs = st.executeQuery();

            while (rs.next()) {
                cliente = clienteMap.get(rs.getInt("id"));

                if (cliente == null) {
                    cliente = Utils.createCliente(rs);
                    list.add(cliente);
                }
            }

            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Cliente> findAll() {

        List<Cliente> list = new ArrayList<>();
        PreparedStatement st = null;
        PreparedStatement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Cliente cliente;
        Endereco endereco;

        try {
            st = conn.prepareStatement("select * from cliente inner join endereco on cliente.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                cliente = clienteMap.get(rs.getInt("id"));
                endereco = enderecoMap.get(rs.getInt("endereco_id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco_id"), endereco);
                }
                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("id"), cliente);
                }
                list.add(cliente);
            }

            st1 = conn.prepareStatement("select * from cliente");

            rs1 = st1.executeQuery();

            while (rs1.next()) {
                cliente = clienteMap.get(rs1.getInt("id"));

                if (cliente == null) {
                    cliente = Utils.createCliente(rs1);
                    list.add(cliente);
                }
            }

            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeResultSet(rs1);
            DB.closeStatement(st);
            DB.closeStatement(st1);
        }
    }

}
