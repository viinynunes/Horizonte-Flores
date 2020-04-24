package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ItemPedido implements Serializable {

    private Integer id;
    private Integer quantidade;
    private Double total;
    private Produto produto;
    private Pedido pedido;

    public ItemPedido() {

    }

    public ItemPedido(Integer id, Integer quantidade, Double total, Produto produto, Pedido pedido) {
        this.id = id;
        this.quantidade = quantidade;
        this.total = total;
        this.produto = produto;
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return id.equals(that.id) &&
                quantidade.equals(that.quantidade) &&
                total.equals(that.total) &&
                produto.equals(that.produto) &&
                pedido.equals(that.pedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantidade, total, produto, pedido);
    }
}
