package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Categoria;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Produto;

public class ProdutoServico {

    public List<Produto> findAll() {

        List<Produto> list = new ArrayList<>();

        Produto produto = new Produto();
        produto.setNome("Buxinho p/24");
        produto.setId(1);
        Fornecedor fornecedor = new Fornecedor();
        Categoria categoria = new Categoria("Vaso", "vs");
        fornecedor.setNome("Planta de Vaso");
        produto.setFornecedorNome(fornecedor.getNome());
        produto.setCategoriaNome(categoria.getNome());

        list.add(produto);

        return list;
    }
}
