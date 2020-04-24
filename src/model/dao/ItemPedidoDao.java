package model.dao;

import model.entities.ItemPedido;
import model.entities.Pedido;

import java.util.List;

public interface ItemPedidoDao {
    void insert (ItemPedido item, Pedido pedido);
    void update (ItemPedido item, Pedido pedido);
    void deleteById (Integer id);
    List<ItemPedido> findAll();
}
