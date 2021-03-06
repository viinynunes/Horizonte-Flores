package model.services;

import model.dao.DaoFactory;
import model.dao.ItemPedidoDao;
import model.entities.Cliente;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.entities.Produto;

import java.sql.Date;
import java.util.List;
import java.util.Set;

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

    public List<ItemPedido> findAll() {
        return dao.findAll();
    }

    public List<ItemPedido> findByProduto(Produto produto){return dao.findByProduto(produto); }

    public Set<ItemPedido> findByData(Cliente cliente, Date iniDate, Date endDate){
        return dao.findByData(cliente, iniDate, endDate);
    }
}
