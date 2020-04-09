package model.util;

import model.entities.Endereco;
import model.entities.Estabelecimento;

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
        estabelecimento.setEndereco(endereco);

        return estabelecimento;
    }
}
