package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Categoria;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Produto;

public class ProdutoServico {

public List<Produto> findAll(){
		
		List<Produto> list = new ArrayList<>();
		
		Categoria categoria = new Categoria("Vaso", "vs");
		Endereco endereco = new Endereco();
		endereco.setId(1);
		endereco.setLogadouro("Rua 1");
		Estabelecimento estabelecimento = new Estabelecimento(1, "Ceaflor", endereco);
		Fornecedor fornecedor = new Fornecedor(1, "Ceaflor", estabelecimento);
		
		list.add(new Produto(1, "Buxinho p/24", categoria, fornecedor));
		
		
		
		return list;
	}
}
