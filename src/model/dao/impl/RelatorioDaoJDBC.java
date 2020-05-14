package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.RelatorioDao;
import model.entities.*;
import model.util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDaoJDBC implements RelatorioDao {

    private Connection conn;

    public RelatorioDaoJDBC(Connection conn){
        this.conn = conn;
    }


    @Override
    public List<Relatorio> findByFornecedor(Fornecedor fornecedor) {
        List<Relatorio> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = conn.prepareStatement("call spRelFindByFornecedor(?)");
            st.setInt(1, fornecedor.getId());

            rs = st.executeQuery();

            while (rs.next()){
                Relatorio rp = Utils.createRelatorioProduto(rs);
                list.add(rp);
            }

            return list;

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Relatorio> findByEstabelecimento(Estabelecimento estabelecimento) {
        List<Relatorio> list = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("call spRelFindByEstabelecimento(?)");

            st.setInt(1, estabelecimento.getId());

            rs = st.executeQuery();

            while (rs.next()){
                Relatorio rp = Utils.createRelatorioProduto(rs);
                list.add(rp);
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
