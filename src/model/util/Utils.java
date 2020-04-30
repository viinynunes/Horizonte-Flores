package model.util;

import model.entities.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static Endereco createEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("endereco_id"));
        endereco.setLogadouro(rs.getString("endereco.logadouro"));
        endereco.setNumero(rs.getString("endereco.numero"));
        endereco.setBairro(rs.getString("endereco.bairro"));
        endereco.setReferencia(rs.getString("endereco.referencia"));
        endereco.setCidade(rs.getString("endereco.cidade"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setPais(rs.getString("pais"));

        return endereco;
    }

    public static Estabelecimento createEstabelecimento(ResultSet rs, Endereco endereco) throws SQLException {

        Estabelecimento estabelecimento = new Estabelecimento();

        estabelecimento.setId(rs.getInt("id"));
        estabelecimento.setNome(rs.getString("nome"));

        if (endereco == null) {
            return estabelecimento;
        } else {
            estabelecimento.setEndereco(endereco);
        }

        return estabelecimento;
    }

    public static Cliente createCliente(ResultSet rs, Endereco endereco) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setTelefone2(rs.getString("telefone2"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCnpj(rs.getString("cnpj"));
        cliente.setEndereco(endereco);

        return cliente;
    }

    public static Cliente createCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setTelefone2(rs.getString("telefone2"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCnpj(rs.getString("cnpj"));

        return cliente;
    }

    public static Produto createProduto(ResultSet rs, Categoria categoria, Fornecedor fornecedor) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("produto.id"));
        produto.setNome(rs.getString("produto.nome"));
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produto;
    }

    public static Categoria createCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("categoria.id"));
        categoria.setNome(rs.getString("categoria.nome"));
        categoria.setAbreviacao(rs.getString("categoria.abreviacao"));

        return categoria;
    }


    public static Fornecedor createFornecedor(ResultSet rs, Estabelecimento estabelecimento) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getInt("fornecedor.id"));
        fornecedor.setNome(rs.getString("fornecedor.nome"));
        fornecedor.setEstabelecimento(estabelecimento);

        return fornecedor;
    }

    public static Pedido createPedido(ResultSet rs, Cliente cliente) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("pedido.id"));
        pedido.setData(rs.getDate("pedido.data"));
        pedido.setCliente(cliente);

        return pedido;
    }

    public static ItemPedido createItemPedido(ResultSet rs, Produto produto, Pedido pedido) throws SQLException {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setId(rs.getInt("itens_do_pedido.id"));
        itemPedido.setPedido(pedido);
        itemPedido.setQuantidade(rs.getInt("itens_do_pedido.quantidade"));
        itemPedido.setProduto(produto);

        return itemPedido;
    }
}
