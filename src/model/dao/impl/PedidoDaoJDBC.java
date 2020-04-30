package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.PedidoDao;
import model.entities.*;
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
        List<ItemPedido> itemPedidoList = new ArrayList<>();
        Endereco endereco;
        Cliente cliente;
        ItemPedido itemPedido;
        Pedido pedido;
        Produto produto;
        Categoria categoria;
        Fornecedor fornecedor;
        Estabelecimento estabelecimento;
/*
        try {
            st = conn.prepareStatement("select * from pedido inner join cliente " +
                    "on pedido.CLIENTE_ID = cliente.id " +
                    "inner join itens_do_pedido " +
                    "on itens_do_pedido.PEDIDO_ID = pedido.id " +
                    "inner join produto " +
                    "on itens_do_pedido.PRODUTO_ID = produto.id " +
                    "inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "inner join endereco " +
                    "on cliente.ENDERECO_ID = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Pedido> pedidoMap = new HashMap<>();
            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Produto> produtoMap = new HashMap<>();
            Map<Integer, ItemPedido> itemPedidoMap = new HashMap<>();

            while (rs.next()) {

                categoria = categoriaMap.get(rs.getInt("categoria.id"));
                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento.id"));
                fornecedor = fornecedorMap.get(rs.getInt("fornecedor.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                itemPedido = itemPedidoMap.get(rs.getInt("itens_do_pedido.id"));
                produto = produtoMap.get(rs.getInt("produto.id"));
                endereco = Utils.createEndereco(rs);

                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }
                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                }
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento.id"), estabelecimento);
                }
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria.id"), categoria);
                }
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor.id"), fornecedor);
                }
                if (produto == null){
                    produto = Utils.createProduto(rs, categoria, fornecedor);
                    produtoMap.put(rs.getInt("produto.id"), produto);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                    list.add(pedido);
                }

                if (itemPedido == null){
                    itemPedido = Utils.createItemPedido(rs, produto, pedido);
                    itemPedidoMap.put(rs.getInt("itens_do_pedido.id"), itemPedido);
                    pedido.addItemPedidoList(itemPedido);
                    itemPedidoList.add(itemPedido);
                }


            }

            return list;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

 */

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
