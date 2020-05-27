package model.entities;

import java.util.Date;

public class Sobra {

    private Integer id;
    private Date data;
    private Integer totalPedido;
    private Integer totalPedidoAtualizado;
    private Integer sobra;
    private Produto produto;

    public Sobra(){
    }

    public Sobra(Integer id, Date data, Integer totalPedido, Integer totalPedidoAtualizado, Integer sobra, Produto produto) {
        this.id = id;
        this.data = data;
        this.totalPedido = totalPedido;
        this.totalPedidoAtualizado = totalPedidoAtualizado;
        this.sobra = sobra;
        this.produto = produto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(Integer totalPedido) {
        this.totalPedido = totalPedido;
    }

    public Integer getTotalPedidoAtualizado() {
        return totalPedidoAtualizado;
    }

    public void setTotalPedidoAtualizado(Integer totalPedidoAtualizado) {
        this.totalPedidoAtualizado = totalPedidoAtualizado;
    }

    public Integer getSobra() {
        return sobra;
    }

    public void setSobra(Integer sobra) {
        this.sobra = sobra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
