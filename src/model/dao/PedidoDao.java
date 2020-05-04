package model.dao;

import model.entities.Pedido;

import java.util.List;

public interface PedidoDao {
    void insert(Pedido pedido);
    void update(Pedido pedido);
    void deleteById(Pedido pedido);
    List<Pedido> findAll();
}
