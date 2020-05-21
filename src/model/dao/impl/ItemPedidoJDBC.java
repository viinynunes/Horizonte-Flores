package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ItemPedidoDao;
import model.entities.*;
import model.util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPedidoJDBC implements ItemPedidoDao {

    private final Connection conn;

    public ItemPedidoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(ItemPedido item) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("Insert into pedido (data, cliente_id) values (?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, new Date((item.getPedido().getData().getTime())));
            st.setInt(2, item.getPedido().getCliente().getId());

            int rows = st.executeUpdate();

            if (rows > 0){
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    item.getPedido().setId(id);
                }
            }
/*
            for (Produto p : item.getListProduto()){

                st = conn.prepareStatement("Insert into Itens_do_pedido (quantidade, pedido_id, produto_id) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                st.setInt(1, p.getQuantidade());
                st.setInt(2, item.getPedido().getId());
                st.setInt(3, p.getId());

                rows = st.executeUpdate();

                if (rows > 0){
                    rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        int id1 = rs.getInt(1);
                        item.setId(id1);
                    }
                }
            }

*/
            conn.commit();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(ItemPedido item) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<ItemPedido> findAll() {

        return null;
    }

    public List<ItemPedido> findAllPedido(Pedido pedido){
        PreparedStatement st = null;
        ResultSet rs = null;
        List<ItemPedido> list = new ArrayList<>();
        Endereco endereco;
        Cliente cliente;
        Estabelecimento estabelecimento;
        Categoria categoria;
        Fornecedor fornecedor;
        Produto produto;
        ItemPedido itemPedido;
/*
        try {
            st = conn.prepareStatement("select * from itens_do_pedido inner join produto " +
                    "on itens_do_pedido.PRODUTO_ID = produto.id " +
                    "inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "inner join pedido " +
                    "on itens_do_pedido.PEDIDO_ID = pedido.id " +
                    "inner join cliente " +
                    "on pedido.CLIENTE_ID = cliente.id " +
                    "inner join endereco " +
                    "on cliente.ENDERECO_ID = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Pedido> pedidoMap = new HashMap<>();
            Map<Integer, Produto> produtoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, ItemPedido> itemPedidoMap = new HashMap<>();
            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();

            while (rs.next()) {
                endereco = enderecoMap.get(rs.getInt("endereco.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));
                categoria = categoriaMap.get(rs.getInt("categoria.id"));
                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento.id"));
                fornecedor = fornecedorMap.get(rs.getInt("fornecedor.id"));
                produto = produtoMap.get(rs.getInt("produto.id"));
                itemPedido = itemPedidoMap.get(rs.getInt("id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco.id"), endereco);
                }

                if (cliente == null) {
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }

                if (categoria == null){
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria.id"), categoria);
                }

                if (estabelecimento == null){
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento.id"), estabelecimento);
                }

                if (fornecedor == null){
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor.id"), fornecedor);
                }

                if (pedido == null) {
                    pedido = Utils.createPedido(rs, cliente);
                    pedidoMap.put(rs.getInt("pedido.id"), pedido);
                }

                if (produto == null){
                    produto = Utils.createProduto(rs,categoria,fornecedor);
                    produtoMap.put(rs.getInt("produto.id"), produto);
                }

                if (itemPedido == null){
                    itemPedido = createItemPedido(rs, pedido, produto);
                    itemPedidoMap.put(rs.getInt("id"), itemPedido);
                }

                list.add(itemPedido);
            }


 */
/*
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

                }


            }
 */

        try {

            st = conn.prepareStatement("SELECT * from itens_do_pedido inner join pedido " +
                    "on itens_do_pedido.PEDIDO_ID = pedido.id " +
                    "inner join produto " +
                    "on itens_do_pedido.PRODUTO_ID = produto.id " +
                    "inner join cliente " +
                    "on pedido.cliente_id = cliente.id " +
                    "inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.ENDERECO_ID = endereco.id " +
                    "where pedido.id = ?");

            st.setInt(1, pedido.getId());

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Cliente> clienteMap = new HashMap<>();
            Map<Integer, Pedido> pedidoMap = new HashMap<>();
            Map<Integer, Produto> produtoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, ItemPedido> itemPedidoMap = new HashMap<>();

            while (rs.next()){

                endereco = enderecoMap.get(rs.getInt("endereco.id"));
                cliente = clienteMap.get(rs.getInt("cliente.id"));
                pedido = pedidoMap.get(rs.getInt("pedido.id"));
                categoria = categoriaMap.get(rs.getInt("categoria.id"));
                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento.id"));
                fornecedor = fornecedorMap.get(rs.getInt("fornecedor.id"));
                produto = produtoMap.get(rs.getInt("produto.id"));

                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("endereco.id"), endereco);
                }
                if (categoria == null){
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria.id"), categoria);
                }
                if (estabelecimento == null){
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento.id"), estabelecimento);
                }
                if (fornecedor == null){
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor.id"), fornecedor);
                }
                if (cliente == null){
                    cliente = Utils.createCliente(rs, endereco);
                    clienteMap.put(rs.getInt("cliente.id"), cliente);
                }
                if (produto == null){
                    produto = Utils.createProduto(rs, categoria, fornecedor);
                    produtoMap.put(rs.getInt("produto.id"), produto);
                }
                itemPedido = Utils.createItemPedido(rs, produto, pedido);
                list.add(itemPedido);

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
    public List<ItemPedido> findByProduto(Produto produto) {
        List<ItemPedido> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Cliente cliente;
        Pedido pedido;
        ItemPedido itemPedido;

        try {
            st = conn.prepareStatement("call spTranFindByProduto(?)");

            st.setInt(1, produto.getId());

            rs = st.executeQuery();

            while (rs.next()){
                cliente = Utils.createCliente(rs);
                pedido = Utils.createPedido(rs, cliente);
                itemPedido = Utils.createItemPedido(rs, produto, pedido);
                list.add(itemPedido);
            }

            return list;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
}
