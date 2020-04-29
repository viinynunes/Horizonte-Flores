package model.util;

import model.entities.Cliente;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.entities.Pedido;

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

    public static Estabelecimento createEstabelecimento(ResultSet rs, Endereco endereco) throws SQLException{

        Estabelecimento estabelecimento = new Estabelecimento();

        estabelecimento.setId(rs.getInt("id"));
        estabelecimento.setNome(rs.getString("nome"));

        if (endereco == null){
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

    public static Pedido createPedido(ResultSet rs, Cliente cliente) throws SQLException{
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setData(rs.getDate("data"));
        pedido.setCliente(cliente);

        return pedido;
    }
}
