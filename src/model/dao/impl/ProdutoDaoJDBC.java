package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ProdutoDao;
import model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoDaoJDBC implements ProdutoDao {

    private Connection conn;

    public ProdutoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Produto produto) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("Insert into produto (nome, categoria_id, fornecedor_id) " +
                    "values (?, ?, ?)");

            st.setString(1, produto.getNome());
            st.setInt(2, produto.getCategoria().getId());
            st.setInt(3, produto.getFornecedor().getId());


            st.executeUpdate();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Produto produto) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("Update produto " +
                    "set nome = ?, categoria_id = ?, fornecedor_id = ?");
            st.setString(1, produto.getNome());
            st.setInt(2, produto.getCategoria().getId());
            st.setInt(3, produto.getFornecedor().getId());

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
            st = conn.prepareStatement("Delete from produto where id = ?");
            st.setInt(1, id);

            st.executeQuery();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Produto findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from hfp.produto inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "where produto.id = ?");

            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows > 0) {
                rs = st.getGeneratedKeys();

                if (rs.next()) {

                    Categoria categoria = createCategoria(rs);
                    Endereco endereco = createEndereco(rs);
                    Estabelecimento estabelecimento = createEstabelecimento(rs, endereco);
                    Fornecedor fornecedor = createFornecedor(rs, estabelecimento);

                    return createProduto(rs, categoria, fornecedor);
                }
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

        return null;
    }


    @Override
    public List<Produto> findByName(String nome) {

        List<Produto> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from hfp.produto inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.ENDERECO_ID = endereco.id " +
                    "where produto.nome = ?");
            st.setString(1, nome);

            rs = st.executeQuery();
            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                Categoria categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                Fornecedor fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                Produto produto = createProduto(rs, categoria, fornecedor);
                list.add(produto);
                return list;
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

        return null;
    }

    @Override
    public List<Produto> findAll() {


        List<Produto> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from produto inner join categoria " +
                    "on produto.CATEGORIA_ID = categoria.id " +
                    "inner join fornecedor " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join estabelecimento " +
                    "on fornecedor.ESTABELECIMENTO_ID = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.ENDERECO_ID = endereco.id");

            rs = st.executeQuery();
            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();

            while (rs.next()) {

                Categoria categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                Fornecedor fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                Produto produto = createProduto(rs, categoria, fornecedor);
                list.add(produto);

            }

            return list;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }

    }

    private Fornecedor createFornecedor(ResultSet rs, Estabelecimento estabelecimento) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setId(rs.getInt("fornecedor.id"));
        fornecedor.setNome(rs.getString("fornecedor.nome"));
        fornecedor.setEstabelecimento(estabelecimento);

        return fornecedor;

    }

    private Estabelecimento createEstabelecimento(ResultSet rs, Endereco endereco) throws SQLException {

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setId(rs.getInt("estabelecimento.id"));
        estabelecimento.setNome(rs.getString("estabelecimento.nome"));
        estabelecimento.setEndereco(endereco);

        return estabelecimento;

    }

    private Categoria createCategoria(ResultSet rs) throws SQLException {

        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("categoria.id"));
        categoria.setNome(rs.getString("categoria.nome"));
        categoria.setAbreviacao(rs.getString("categoria.abreviacao"));

        return categoria;
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

    private Produto createProduto(ResultSet rs, Categoria categoria, Fornecedor fornecedor) throws SQLException {

        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produto;

    }
}
