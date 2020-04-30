package model.services;

import model.dao.DaoFactory;
import model.dao.ItemPedidoDao;
import model.entities.ItemPedido;
import model.entities.Pedido;

import java.util.List;

public class ItemPedidoServico {

    private ItemPedidoDao dao = DaoFactory.createItemDao();

    public void insert (ItemPedido item){
        dao.insert(item);
    }

    public List<ItemPedido> findall() {
        return dao.findAll();
    }

    public List<ItemPedido> findAllPedidos(Pedido pedido){
        return dao.findAllPedido(pedido);
    }
}
