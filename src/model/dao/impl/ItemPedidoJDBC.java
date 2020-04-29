package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ItemPedidoDao;
import model.entities.ItemPedido;
import model.entities.Produto;

import java.sql.*;
import java.util.List;

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
}
