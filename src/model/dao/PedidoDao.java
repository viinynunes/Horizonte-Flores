package model.dao;

import model.entities.Pedido;

import java.util.List;

public interface PedidoDao {
    List<Pedido> findAll();
}
