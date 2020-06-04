package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.PedidoDao;
import model.entities.Cliente;
import model.entities.Endereco;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.util.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDaoJDBC implements PedidoDao {

    private Connection conn;

    public PedidoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Pedido> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        List<Pedido> list = new ArrayList<>();
        Endereco endereco;
        Cliente cliente;
        Pedido pedido;

        try {
            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on CLIENTE_ID = cliente.id " +
                    "inner join endereco " +
                    "on cliente.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Pedido> pedidoMap = new HashMap<>();

            while (rs.next()) {
                endereco = enderecoMap.get(rs.getInt("endereco.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco.id"), endereco);
                }

                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }
            }


            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on pedido.CLIENTE_ID = cliente.id");

            rs = st.executeQuery();

            while (rs.next()) {

                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (cliente == null) {
                    cliente = Utils.createCliente(rs);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
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
    public List<Pedido> findByDate(Date date) {
        List<Pedido> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Cliente cliente;
        Pedido pedido;

        try {

            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on CLIENTE_ID = cliente.id " +
                    "inner join endereco " +
                    "on cliente.endereco_id = endereco.id " +
                    "where data = ? " +
                    "order by pedido.id");

            st.setDate(1, date);
            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Pedido> pedidoMap = new HashMap<>();

            while (rs.next()) {
                endereco = enderecoMap.get(rs.getInt("endereco.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco.id"), endereco);
                }

                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }
            }


            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on pedido.CLIENTE_ID = cliente.id " +
                    "where data = ? " +
                    "order by pedido.id");

            st.setDate(1, date);

            rs = st.executeQuery();

            while (rs.next()) {

                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (cliente == null) {
                    cliente = Utils.createCliente(rs);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }
            }

            return list;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Pedido> findByDate(Date iniDate, Date endDate) {
        List<Pedido> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Cliente cliente;
        Pedido pedido;

        try {

            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on CLIENTE_ID = cliente.id " +
                    "inner join endereco " +
                    "on cliente.endereco_id = endereco.id " +
                    "where data between ? and ?");

            st.setDate(1, iniDate);
            st.setDate(2, endDate);

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Pedido> pedidoMap = new HashMap<>();

            while (rs.next()) {
                endereco = enderecoMap.get(rs.getInt("endereco.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco.id"), endereco);
                }

                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }
            }


            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on pedido.CLIENTE_ID = cliente.id " +
                    "where data between ? and ?");

            st.setDate(1, iniDate);
            st.setDate(2, endDate);

            rs = st.executeQuery();

            while (rs.next()) {

                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));

                if (cliente == null) {
                    cliente = Utils.createCliente(rs);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }
            }

            return list;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void insert(Pedido pedido) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement("insert into pedido (data, cliente_id) values (?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, new Date(pedido.getData().getTime()));
            st.setInt(2, pedido.getCliente().getId());

            int rows = st.executeUpdate();

            if (rows > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pedido.setId(id);
                }
            }

            for (ItemPedido i : pedido.getItemPedidoList()) {

                st = conn.prepareStatement("insert into itens_do_pedido (quantidade, pedido_id, produto_id) " +
                        "values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                st.setInt(1, i.getQuantidade());
                st.setInt(2, pedido.getId());
                st.setInt(3, i.getProduto().getId());

                rows = st.executeUpdate();

                if (rows > 0) {
                    rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        i.setId(id);
                    }
                }
            }

            for (ItemPedido item : pedido.getItemPedidoList()){
                st = conn.prepareStatement("insert into sobra (data, produto_id, sobra, pedido_id, totalPedido) " +
                        "values (?, ?, 0, ?, ?)");

                st.setDate(1, new Date(pedido.getData().getTime()));
                st.setInt(2, item.getProduto().getId());
                st.setInt(3, pedido.getId());
                st.setInt(4, item.getQuantidade());


                st.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Pedido pedido) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement("delete from itens_do_pedido " +
                    "where pedido_id = ?");

            st.setInt(1, pedido.getId());

            st.executeUpdate();

            st = conn.prepareStatement("delete from sobra where pedido_id = ?");
            st.setInt(1, pedido.getId());

            st.executeUpdate();

            for (ItemPedido i : pedido.getItemPedidoList()) {
                st = conn.prepareStatement("insert into itens_do_pedido (quantidade, pedido_id, produto_id) " +
                        "values (?, ?, ?) ");

                st.setInt(1, i.getQuantidade());
                st.setInt(2, pedido.getId());
                st.setInt(3, i.getProduto().getId());

                st.executeUpdate();
            }

            for (ItemPedido item : pedido.getItemPedidoList()){
                st = conn.prepareStatement("insert into sobra (data, produto_id, sobra, pedido_id, totalPedido) " +
                        "values (?, ?, 0, ?, ?)");

                st.setDate(1, new Date(pedido.getData().getTime()));
                st.setInt(2, item.getProduto().getId());
                st.setInt(3, pedido.getId());
                st.setInt(4, item.getQuantidade());
                st.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Pedido pedido) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement("delete from itens_do_pedido " +
                    "where pedido_id = ?");

            st.setInt(1, pedido.getId());

            st.executeUpdate();

            st = conn.prepareStatement("delete from pedido where id = ?");

            st.setInt(1, pedido.getId());

            st.executeUpdate();

            st = conn.prepareStatement("delete from sobra where pedido_id = ?");
            st.setInt(1, pedido.getId());

            st.executeUpdate();

            conn.commit();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

}
