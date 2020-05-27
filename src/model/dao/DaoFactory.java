package model.dao;

import com.mysql.jdbc.Connection;
import db.DB;
import model.dao.impl.*;

public class DaoFactory {

    public static ClienteDao createClienteDao() {
        return new ClienteDaoJDBC(DB.getConnection());
    }

    public static ProdutoDao createProdutoDao() {
        return new ProdutoDaoJDBC(DB.getConnection());
    }

    public static CategoriaDao createCategoriaDao() {
        return new CategoriaDaoJDBC(DB.getConnection());
    }

    public static EstabelecimentoDao createEstabelecimentoDao() { return new EstabelecimentoDaoJDBC(DB.getConnection()); }

    public static FornecedorDao createFornecedorDao() { return new FornecedorDaoJDBC(DB.getConnection()); }

    public static EnderecoDao createEnderecoDao() {return new EnderecoDaoJDBC(DB.getConnection()); }

    public static ItemPedidoDao createItemDao() {return  new ItemPedidoJDBC(DB.getConnection()); }

    public static PedidoDao createPedidoDao() {return new PedidoDaoJDBC(DB.getConnection()); }

    public static RelatorioDao createRelatorioDao() { return new RelatorioDaoJDBC(DB.getConnection()); }

    public static SobraDao createSobraDao() {
        return new SobraDaoJDBC((Connection) DB.getConnection());
    }
}
