package model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemPedido implements Serializable {

    private Integer id;
    private Integer quantidade;
    private Pedido pedido;
    private Produto produto;

    public ItemPedido() {

    }

    public ItemPedido(Integer id, Integer quantidade, Pedido pedido, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.pedido = pedido;
        this.produto = produto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
