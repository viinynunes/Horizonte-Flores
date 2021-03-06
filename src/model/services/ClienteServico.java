package model.services;

import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.entities.Cliente;

import java.sql.Date;
import java.util.List;

public class ClienteServico {

    private ClienteDao dao = DaoFactory.createClienteDao();

    public List<Cliente> findAll(){

        List<Cliente> list = dao.findAll();

        return list;
    }

    public List<Cliente> findByData(Date date){
        return dao.findByData(date);
    }

    public void saveOrUpdate(Cliente cliente){
        if (cliente.getId() == null){
            dao.insert(cliente);
        }else{
            dao.update(cliente);
        }
    }

    public void deleteById(Integer id){
        dao.deleteById(id);
    }
}
