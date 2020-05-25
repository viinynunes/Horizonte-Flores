package model.services;

import model.dao.DaoFactory;
import model.dao.EstabelecimentoDao;
import model.entities.Estabelecimento;

import java.util.List;

public class EstabelecimentoServico {

    private EstabelecimentoDao dao = DaoFactory.createEstabelecimentoDao();

    public List<Estabelecimento> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Estabelecimento estabelecimento){
        if (estabelecimento.getId() == null){
            dao.insert(estabelecimento);
        } else {
            dao.update(estabelecimento);
        }
    }

    public Estabelecimento findById(Integer id){
        return dao.findById(id);
    }
}
