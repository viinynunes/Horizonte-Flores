package model.dao.impl;

import db.DBException;
import model.dao.ItemPedidoDao;
import model.entities.ItemPedido;
import model.entities.Pedido;

import java.sql.*;
import java.util.List;

public class ItemPedidoJDBC implements ItemPedidoDao {

    private Connection conn;

    public ItemPedidoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(ItemPedido item, Pedido pedido) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("Insert into pedido (data, cliente_id) values (?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, new Date((pedido.getData().getTime())));
            st.setInt(2, pedido.getCliente().getId());

            int rows = st.executeUpdate();

            if (rows > 0){
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pedido.setId(id);
                }
            }

            st = conn.prepareStatement("Insert into Itens_do_pedido (quantidade, pedido_id, produto_id) values " +
                    "(?, ?, ?)");

            st.setInt(1, item.getQuantidade());
            st.setInt(2, item.getPedido().getId());
            st.setInt(3, item.getProduto().getId());

            rows = st.executeUpdate();

            if (rows > 0){
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    item.setId(id);
                }
            }

            conn.commit();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public void update(ItemPedido item, Pedido pedido) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<ItemPedido> findAll() {
        return null;
    }
}
