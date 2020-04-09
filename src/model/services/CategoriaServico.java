package model.services;

import model.dao.CategoriaDao;
import model.dao.DaoFactory;
import model.entities.Categoria;

import java.util.List;

public class CategoriaServico {

    CategoriaDao dao = DaoFactory.createCategoriaDao();

    public List<Categoria> findAll (){

        List<Categoria> list = dao.findAll();

        return list;
    }


}
