package model.entities;

public class Relatorio {
    private Integer id;
    private String nome;
    private Integer quantidade;
    private String fornecedor;

    public Relatorio(){

    }

    public Relatorio(Integer id, String nome, Integer quantidade, String fornecedor) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.fornecedor = fornecedor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}
