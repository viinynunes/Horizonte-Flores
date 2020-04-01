package model.dao;

import db.DB;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.ProdutoDaoJDBC;

public class DaoFactory {

    public ClienteDao createClienteDao() {
        return new ClienteDaoJDBC(DB.getConnection());
    }

    public ProdutoDao createProdutoDao(){
        return new ProdutoDaoJDBC(DB.getConnection());
    }
}
