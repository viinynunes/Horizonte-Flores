package model.services;

import model.dao.DaoFactory;
import model.dao.PedidoDao;
import model.entities.Pedido;

import java.util.List;

public class PedidoServico {

    private final PedidoDao dao = DaoFactory.createPedido();

    public List<Pedido> findAll(){
        return dao.findAll();
    }
}
