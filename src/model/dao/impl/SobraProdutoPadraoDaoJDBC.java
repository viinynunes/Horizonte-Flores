package model.dao.impl;

import application.Main;
import db.DB;
import db.DBException;
import model.dao.SobraProdutoPadraoDao;
import model.entities.*;
import model.util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SobraProdutoPadraoDaoJDBC implements SobraProdutoPadraoDao {
    private Connection conn;

    public SobraProdutoPadraoDaoJDBC(Connection conn){
        this.conn = conn;
    }


    @Override
    public void insert(SobraProdutoPadrao sobraProdutoPadrao) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("insert into sobraprodutopadrao (produto_id) values (?)", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, sobraProdutoPadrao.getProduto().getId());

            int row = st.executeUpdate();

            if (row > 0){
                rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    sobraProdutoPadrao.setId(id);
                }
            }

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(SobraProdutoPadrao sobraProdutoPadrao) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<SobraProdutoPadrao> findAll() {
        return null;
    }

    @Override
    public List<SobraProdutoPadrao> findByFornecedor(Fornecedor fornecedor) {
        List<SobraProdutoPadrao> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        SobraProdutoPadrao padrao;

        try {
            st = conn.prepareStatement("select * from sobraprodutopadrao inner join produto " +
                    "on sobraprodutopadrao.produto_id = produto.id " +
                    "inner join categoria on produto.categoria_id = categoria.id " +
                    "inner join fornecedor on produto.fornecedor_id = fornecedor.id " +
                    "inner join estabelecimento on fornecedor.estabelecimento_id = estabelecimento.id " +
                    "inner join endereco on estabelecimento.endereco_id = endereco.id " +
                    "where fornecedor.id = ? " +
                    "order by produto.nome");

            st.setInt(1, fornecedor.getId());

            rs = st.executeQuery();

            Map<Integer, Categoria> categoriaMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();
            Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Produto> produtoMap = new HashMap<>();

            while (rs.next()) {
                Categoria categoria = categoriaMap.get(rs.getInt("categoria_id"));
                if (categoria == null) {
                    categoria = Utils.createCategoria(rs);
                    categoriaMap.put(rs.getInt("categoria_id"), categoria);
                }

                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null) {
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("estabelecimento_id"));
                if (estabelecimento == null) {
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("estabelecimento_id"), estabelecimento);
                }

                fornecedor = fornecedorMap.get(rs.getInt("fornecedor_id"));
                if (fornecedor == null) {
                    fornecedor = Utils.createFornecedor(rs, estabelecimento);
                    fornecedorMap.put(rs.getInt("fornecedor_id"), fornecedor);
                }

                Produto produto = produtoMap.get(rs.getInt("produto.id"));
                if (produto == null){
                    produto = Utils.createProduto(rs, categoria, fornecedor);
                    produtoMap.put(rs.getInt("produto.id"), produto);
                }

                padrao = Utils.createSobraProdutoPadrao(rs, produto);
                list.add(padrao);
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
