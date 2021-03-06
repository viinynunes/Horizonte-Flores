package model.services;

import model.dao.DaoFactory;
import model.dao.PedidoDao;
import model.entities.Pedido;

import java.sql.Date;
import java.util.List;

public class PedidoServico {

    private final PedidoDao dao = DaoFactory.createPedidoDao();

    public List<Pedido> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Pedido pedido){
        if (pedido.getId() == null){
            dao.insert(pedido);
        } else {
            dao.update(pedido);
        }
    }

    public void deleteById(Pedido pedido){
        dao.deleteById(pedido);
    }

    public List<Pedido> findByDate(Date iniDate, Date endDate) {
        return dao.findByDate(iniDate,endDate);
    }

    public List<Pedido> findByDate(Date date) {
        return dao.findByDate(date);
    }
}
