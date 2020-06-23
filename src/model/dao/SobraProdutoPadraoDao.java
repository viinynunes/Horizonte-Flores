package model.dao;

import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.SobraProdutoPadrao;

import java.util.List;

public interface SobraProdutoPadraoDao {
    void insert(SobraProdutoPadrao sobraProdutoPadrao);
    void update(SobraProdutoPadrao sobraProdutoPadrao);
    void deleteById(Integer id);
    void deleteByProduto(Produto produto);
    List<SobraProdutoPadrao> findAll();
    List<SobraProdutoPadrao> findByFornecedor(Fornecedor fornecedor);
}
