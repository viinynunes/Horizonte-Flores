package model.dao;

import model.entities.Fornecedor;
import model.entities.Relatorio;

import java.util.List;

public interface RelatorioDao {
    List<Relatorio> findByFornecedor(Fornecedor fornecedor);
}
