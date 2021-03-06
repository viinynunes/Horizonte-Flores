package model.dao;

import model.entities.Cliente;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.entities.Produto;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface ItemPedidoDao {
    void insert (ItemPedido item);
    void update (ItemPedido item);
    void deleteById (Integer id);
    List<ItemPedido> findAll();
    List<ItemPedido> findAllPedido(Pedido pedido);
    List<ItemPedido> findByProduto(Produto produto);
    Set<ItemPedido> findByData(Cliente cliente, Date iniDate, Date endDate);
}
