package model.dao;

import model.entities.Cliente;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Relatorio;

import java.sql.Date;
import java.util.List;

public interface RelatorioDao {
    List<Relatorio> findByFornecedorAndCliente(Fornecedor fornecedor, Cliente cliente, Date date);
    List<Relatorio> findByFornecedor(Fornecedor fornecedor, Date iniDate, Date endDate);
    List<Relatorio> findByEstabelecimento(Estabelecimento estabelecimento, Date iniDate, Date endDate);
}
