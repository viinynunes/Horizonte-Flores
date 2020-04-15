package model.services;

import model.dao.CategoriaDao;
import model.dao.DaoFactory;
import model.entities.Categoria;

import java.util.List;

public class CategoriaServico {

    CategoriaDao dao = DaoFactory.createCategoriaDao();

    public List<Categoria> findAll (){
        return dao.findAll();
    }

    public void saveOrUpdate(Categoria categoria){
        if (categoria.getId() == null){
            dao.insert(categoria);
        } else {
            dao.update(categoria);
        }
    }


}
