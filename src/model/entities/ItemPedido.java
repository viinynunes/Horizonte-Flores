package model.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ItemPedido implements Serializable {

    private Integer id;
    private Integer quantidade;
    private Double total;
    private Pedido pedido;
    private List<Produto> listProduto;

    public ItemPedido() {

    }

    public ItemPedido(Integer id, Integer quantidade, Double total, Pedido pedido) {
        this.id = id;
        this.quantidade = quantidade;
        this.total = total;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Produto> getListProduto() {
        return listProduto;
    }

    public void setListProduto(List<Produto> listProduto) {
        this.listProduto = listProduto;
    }
}
