package model.services;

import model.dao.DaoFactory;
import model.dao.SobraDao;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Sobra;

import java.sql.Date;
import java.util.List;

public class SobraServico {

    private SobraDao dao = DaoFactory.createSobraDao();

    public void insertOrUpdate(Sobra sobra){
        if (sobra.getId() == null){
            dao.insert(sobra);
        } else {
            dao.update(sobra);
        }
    }

    public void deleteById(Integer id){
        dao.deleteById(id);
    }

    public void deleteByDataAndProduto(Date iniDate, Date endDate, Integer id){
        dao.deleteByDateAndProdutoId(iniDate, endDate, id);
    }

    public List<Sobra> findByDate(Date date, Date date1){
        return dao.findByData(date, date1);
    }

    public List<Sobra> findAll(){
        return dao.findAll();
    }

    public List<Sobra> findByFornecedor(Fornecedor fornecedor, Date iniDate, Date endDate) {
        return dao.findByFornecedor(fornecedor, iniDate, endDate);
    }

    public List<Sobra> findByEstabelecimento(Estabelecimento estabelecimento, Date iniDate, Date endDate) {
        return dao.findByEstabelecimento(estabelecimento, iniDate, endDate);
    }
}
