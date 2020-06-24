package model.dao.impl;

import com.mysql.jdbc.Connection;
import db.DB;
import db.DBException;
import model.dao.SobraDao;
import model.entities.*;
import model.util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SobraDaoJDBC implements SobraDao {
    private Connection conn;

    public SobraDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Sobra sobra) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("insert into sobra (data, produto_id, sobra, pedido_id, totalPedido, totalPedidoAtualizado) " +
                    "values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, new Date(sobra.getData().getTime()));
            st.setInt(2, sobra.getProduto().getId());
            st.setInt(3, sobra.getSobra());
            st.setInt(4, 0);
            st.setInt(5, sobra.getTotalPedido());
            st.setInt(6, sobra.getTotalPedidoAtualizado());


            int row = st.executeUpdate();

            if (row > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    sobra.setId(id);
                }
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Sobra sobra) {
        PreparedStatement st = null;

        try {

            conn.setAutoCommit(false);
            st = conn.prepareStatement("Update sobra set totalPedidoAtualizado = ?, sobra = ? where id = ?");

            st.setInt(1, sobra.getTotalPedidoAtualizado());
            st.setInt(2, sobra.getSobra());
            st.setInt(3, sobra.getId());

            st.executeUpdate();

            conn.commit();
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
            st = conn.prepareStatement("delete from sobra where id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByDateAndProdutoId(Date iniDate, Date endDate, Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("delete from sobra where data between ? and ? and produto_id = ?");

            st.setDate(1, iniDate);
            st.setDate(2, endDate);
            st.setInt(3, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Sobra> findByData(Date data, Date data2) {
        List<Sobra> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Categoria categoria;
        Estabelecimento estabelecimento;
        Fornecedor fornecedor;
        Produto produto;
        Sobra sobra;

        try {

            st = conn.prepareStatement("select *, sum(totalPedido) as sumTotal, sum(totalPedidoAtualizado) as sum from sobra " +
                    "where data between ? and ? " +
                    "group by produto_id");

            st.setDate(1, data);
            st.setDate(2, data2);

            rs = st.executeQuery();

            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                produto = Utils.createProduto(rs, categoria, fornecedor);

                sobra = Utils.createSobra(rs, produto);

                list.add(sobra);

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
    public List<Sobra> findAll(){
        List<Sobra> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Categoria categoria;
        Estabelecimento estabelecimento;
        Fornecedor fornecedor;
        Produto produto;
        Sobra sobra;

        try {

            st = conn.prepareStatement("select * from sobra inner join produto " +
                    "on sobra.produto_id = produto.id " +
                    "inner join categoria " +
                    "on produto.categoria_id = categoria.id " +
                    "inner join fornecedor " +
                    "on produto.fornecedor_id = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.estabelecimento_id = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                produto = Utils.createProduto(rs, categoria, fornecedor);

                sobra = Utils.createSobra(rs, produto);

                list.add(sobra);


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
    public List<Sobra> findByFornecedor(Fornecedor fornecedor, Date iniDate, Date endDate) {
        List<Sobra> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Categoria categoria;
        Estabelecimento estabelecimento;
        Produto produto;
        Sobra sobra;

        try {

            st = conn.prepareStatement("call spSelectSobraEntreDataByFornecedor(?, ?, ?)");

            st.setInt(1, fornecedor.getId());
            st.setDate(2, iniDate);
            st.setDate(3, endDate);

            rs = st.executeQuery();

            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                produto = Utils.createProduto(rs, categoria, fornecedor);

                sobra = Utils.createSobra(rs, produto);

                list.add(sobra);
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
    public List<Sobra> findByEstabelecimento(Estabelecimento estabelecimento, Date iniDate, Date endDate) {
        List<Sobra> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        Endereco endereco;
        Categoria categoria;
        Fornecedor fornecedor;
        Produto produto;
        Sobra sobra;

        try {

            st = conn.prepareStatement("call spSelectSobraEntreDataByEstabelecimento(?, ?, ?)");

            st.setInt(1, estabelecimento.getId());
            st.setDate(2, iniDate);
            st.setDate(3, endDate);

            rs = st.executeQuery();

            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                produto = Utils.createProduto(rs, categoria, fornecedor);

                sobra = Utils.createSobra(rs, produto);

                list.add(sobra);
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
