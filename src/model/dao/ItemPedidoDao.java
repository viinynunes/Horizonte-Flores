package model.dao;

import model.entities.ItemPedido;
import model.entities.Pedido;

import java.util.List;

public interface ItemPedidoDao {
    void insert (ItemPedido item);
    void update (ItemPedido item);
    void deleteById (Integer id);
    List<ItemPedido> findAll();
}
