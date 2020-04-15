package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.FornecedorDao;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

                Fornecedor fornecedor = createFornecedor(rs, estabelecimento);
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

    private Fornecedor createFornecedor(ResultSet rs, Estabelecimento estabelecimento) throws SQLException{

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setId(rs.getInt("id"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setEstabelecimento(estabelecimento);

        return fornecedor;

    }
}
