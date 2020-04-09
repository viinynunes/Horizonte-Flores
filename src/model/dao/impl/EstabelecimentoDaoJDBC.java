package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.EstabelecimentoDao;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.util.Utils;

import java.lang.reflect.GenericDeclaration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstabelecimentoDaoJDBC implements EstabelecimentoDao {

    private Connection conn;
    private PreparedStatement st = null;

    public EstabelecimentoDaoJDBC (Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Estabelecimento estabelecimento) {
        try {

            st = conn.prepareStatement("insert into estabelecimento (nome, endereco_id) values (?, ?)");
            st.setString(1, estabelecimento.getNome());
            st.setInt(2, estabelecimento.getEndereco().getId());

            st.executeQuery();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Estabelecimento estabelecimento) {

        try {
            st = conn.prepareStatement("Update estabelecimento set nome = ?, endereco_id = ? where id = ?");

            st.setString(1, estabelecimento.getNome());
            st.setInt(2, estabelecimento.getEndereco().getId());
            st.setInt(3, estabelecimento.getId());

            st.executeQuery();
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {

        try {
            st = conn.prepareStatement("delete from estabelecimento where id = ?");
            st.setInt(1, id);

            st.executeQuery();
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public List<Estabelecimento> findAll() {
        ResultSet rs = null;
        List<Estabelecimento> list = new ArrayList<>();

        try {
            st = conn.prepareStatement("select * from estabelecimento inner join endereco on " +
                    "estabelecimento.endereco_id = endereco.id");

            rs = st.executeQuery();

            Map<Integer, Endereco> map = new HashMap<>();

            while (rs.next()){

                Endereco endereco = map.get(rs.getInt("endereco_id"));
                if (endereco == null){
                    endereco = Utils.createEndereco(rs);
                    map.put(rs.getInt("endereco_id"), endereco);
                }

                Estabelecimento estabelecimento = Utils.createEstabelecimento(rs, endereco);
                list.add(estabelecimento);
            }

            return list;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }
    }

}
