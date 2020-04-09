package model.services;

import model.dao.DaoFactory;
import model.dao.EstabelecimentoDao;
import model.entities.Estabelecimento;

import java.util.List;

public class EstabelecimentoServico {

    EstabelecimentoDao dao = DaoFactory.createEstabelecimentoDao();

    public List<Estabelecimento> findAll(){
        return dao.findAll();
    }
}
