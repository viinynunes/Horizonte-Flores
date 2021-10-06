package model.entities;

import java.io.Serializable;

public class Produto implements Serializable, Comparable<Produto> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Integer quantidade;
    private Categoria categoria;
    private Fornecedor fornecedor;

    public Produto() {

    }

    public Produto(Integer id, String nome, Categoria categoria, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (nome == null) {
            return other.nome == null;
        } else return nome.equals(other.nome);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Produto o) {
        return this.id.compareTo(o.getId());
    }
}
