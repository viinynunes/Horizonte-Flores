package model.services;

import model.dao.DaoFactory;
import model.dao.RelatorioDao;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Relatorio;

import java.util.List;

public class RelatorioServico {
    RelatorioDao dao = DaoFactory.createRelatorioDao();

    public List<Relatorio> findByFornecedor(Fornecedor fornecedor){
        return dao.findByFornecedor(fornecedor);
    }
    public List<Relatorio> findByEstabelecimento(Estabelecimento estabelecimento){
        return dao.findByEstabelecimento(estabelecimento);
    }
}
