package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.PedidoDao;
import model.entities.Cliente;
import model.entities.Endereco;
import model.entities.Pedido;
import model.util.Utils;

import java.sql.*;
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

}
