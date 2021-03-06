package model.dao;

import model.entities.Pedido;

import java.sql.Date;
import java.util.List;

public interface PedidoDao {
    void insert(Pedido pedido);
    void update(Pedido pedido);
    void deleteById(Pedido pedido);
    List<Pedido> findAll();
    List<Pedido> findByDate(Date date);
    List<Pedido> findByDate(Date iniDate, Date endDate);
}
