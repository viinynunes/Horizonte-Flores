package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.FornecedorDao;
import model.entities.Cliente;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FornecedorDaoJDBC implements FornecedorDao {

    private Connection conn;
    private PreparedStatement st = null;

    public FornecedorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Fornecedor fornecedor) {

        try {
            st = conn.prepareStatement("insert into fornecedor (nome, estabelecimento_id) values (?, ?)");

            st.setString(1, fornecedor.getNome());
            st.setInt(2, fornecedor.getEstabelecimento().getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Fornecedor fornecedor) {

        try {
            st = conn.prepareStatement("update fornecedor set nome = ?, estabelecimento_id = ? where id = ?");

            st.setString(1, fornecedor.getNome());
            st.setInt(2, fornecedor.getEstabelecimento().getId());
            st.setInt(3, fornecedor.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        try {
            st = conn.prepareStatement("delete from fornecedor where id = ?");
            st.setInt(1, id);

            st.executeQuery();
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public List<Fornecedor> findAll() {
        List<Fornecedor> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from fornecedor inner join estabelecimento " +
                    "on fornecedor.estabelecimento_id = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();

            while (rs.next()){
                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null){
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("fornecedor.estabelecimento_id"));
                if (estabelecimento == null){
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("fornecedor.estabelecimento_id"), estabelecimento);
                }

                Fornecedor fornecedor = Utils.createFornecedor(rs, estabelecimento);
                list.add(fornecedor);
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
    public List<Fornecedor> findByData(Date iniDate, Date endDate) {
        List<Fornecedor> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from fornecedor inner join estabelecimento " +
                    "on fornecedor.estabelecimento_id = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.endereco_id = endereco.id " +
                    "inner join produto " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join itens_do_pedido " +
                    "on itens_do_pedido.PRODUTO_ID = produto.id " +
                    "inner join pedido " +
                    "on itens_do_pedido.PEDIDO_ID = pedido.id " +
                    "where pedido.data between ? and ? " +
                    "group by fornecedor.nome " +
                    "order by fornecedor.id");

            st.setDate(1, iniDate);
            st.setDate(2, endDate);

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();

            while (rs.next()){
                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null){
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("fornecedor.estabelecimento_id"));
                if (estabelecimento == null){
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("fornecedor.estabelecimento_id"), estabelecimento);
                }

                Fornecedor fornecedor = Utils.createFornecedor(rs, estabelecimento);
                list.add(fornecedor);
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
    public List<Fornecedor> findByClienteAndData(Cliente cliente, Date date) {
        List<Fornecedor> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select * from fornecedor inner join estabelecimento " +
                    "on fornecedor.estabelecimento_id = estabelecimento.id " +
                    "inner join endereco " +
                    "on estabelecimento.endereco_id = endereco.id " +
                    "inner join produto " +
                    "on produto.FORNECEDOR_ID = fornecedor.id " +
                    "inner join itens_do_pedido " +
                    "on itens_do_pedido.PRODUTO_ID = produto.id " +
                    "inner join pedido " +
                    "on itens_do_pedido.PEDIDO_ID = pedido.id " +
                    "inner join cliente " +
                    "on pedido.cliente_id = cliente.id " +
                    "where cliente.id = ? and pedido.data = ? " +
                    "group by fornecedor.nome " +
                    "order by fornecedor.id");

            st.setInt(1, cliente.getId());
            st.setDate(2, date);

            rs = st.executeQuery();

            Map<Integer, Endereco> enderecoMap = new HashMap<>();
            Map<Integer, Estabelecimento> estabelecimentoMap = new HashMap<>();

            while (rs.next()){
                Endereco endereco = enderecoMap.get(rs.getInt("estabelecimento.endereco_id"));
                if (endereco == null){
                    endereco = Utils.createEndereco(rs);
                    enderecoMap.put(rs.getInt("estabelecimento.endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = estabelecimentoMap.get(rs.getInt("fornecedor.estabelecimento_id"));
                if (estabelecimento == null){
                    estabelecimento = Utils.createEstabelecimento(rs, endereco);
                    estabelecimentoMap.put(rs.getInt("fornecedor.estabelecimento_id"), estabelecimento);
                }

                Fornecedor fornecedor = Utils.createFornecedor(rs, estabelecimento);
                list.add(fornecedor);
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
