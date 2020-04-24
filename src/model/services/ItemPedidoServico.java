package model.services;

import model.dao.DaoFactory;
import model.dao.ItemPedidoDao;
import model.entities.ItemPedido;
import model.entities.Pedido;

public class ItemPedidoServico {

    private ItemPedidoDao dao = DaoFactory.createItemDao();

    public void insert (ItemPedido item, Pedido pedido){
        dao.insert(item, pedido);
    }
}
