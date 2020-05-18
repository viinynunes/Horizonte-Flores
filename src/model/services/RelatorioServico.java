package model.services;

import model.dao.DaoFactory;
import model.dao.RelatorioDao;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Relatorio;

import java.sql.Date;
import java.util.List;

public class RelatorioServico {
    RelatorioDao dao = DaoFactory.createRelatorioDao();

    public List<Relatorio> findByFornecedor(Fornecedor fornecedor,  Date iniDate, Date endDate){

        return dao.findByFornecedor(fornecedor, iniDate, endDate);
    }
    public List<Relatorio> findByEstabelecimento(Estabelecimento estabelecimento, Date iniDate, Date endDate){
        return dao.findByEstabelecimento(estabelecimento, iniDate, endDate);
    }
}
