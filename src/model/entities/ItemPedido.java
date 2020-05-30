package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ItemPedido implements Serializable, Comparable<ItemPedido>{

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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
        result = prime * result + ((produto == null) ? 0 : produto.hashCode());
        result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
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
        ItemPedido other = (ItemPedido) obj;
        if (pedido == null) {
            if (other.pedido != null)
                return false;
        } else if (!pedido.equals(other.pedido))
            return false;
        if (produto == null) {
            if (other.produto != null)
                return false;
        } else if (!produto.equals(other.produto))
            return false;
        if (quantidade == null) {
            if (other.quantidade != null)
                return false;
        } else if (!quantidade.equals(other.quantidade))
            return false;
        return true;
    }

    @Override
    public int compareTo(ItemPedido o) {
        return id.compareTo(o.id);
    }
}
