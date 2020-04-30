package model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Pedido {

    private Integer id;
    private Date Data;
    private Cliente cliente;

    private List<ItemPedido> itemPedidoList = new ArrayList<>();

    public Pedido (){

    }

    public Pedido(Integer id, Date data, Cliente cliente) {
        this.id = id;
        Data = data;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void addItemPedidoList(ItemPedido itemPedido){
        itemPedidoList.add(itemPedido);
    }

    public void removeItemPedidoList(ItemPedido itemPedido){
        itemPedidoList.remove(itemPedido);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id.equals(pedido.id) &&
                Data.equals(pedido.Data) &&
                cliente.equals(pedido.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Data, cliente);
    }
}
