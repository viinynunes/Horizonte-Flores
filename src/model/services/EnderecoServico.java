package model.services;

import model.dao.DaoFactory;
import model.dao.EnderecoDao;
import model.entities.Endereco;

public class EnderecoServico {

    private EnderecoDao dao = DaoFactory.createEnderecoDao();

    public void saveOrUpdate (Endereco endereco){
        if (endereco.getId() == null){
            dao.insert(endereco);
        } else {
            dao.update(endereco);
        }
    }
}
