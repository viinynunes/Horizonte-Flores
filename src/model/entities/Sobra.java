package model.entities;

import java.util.Date;

public class Sobra {

    private Integer id;
    private Date data;
    private Integer total;
    private Integer sobra;
    private Produto produto;

    public Sobra(){
    }

    public Sobra(Integer id, Date data, Integer total, Integer sobra, Produto produto) {
        this.id = id;
        this.data = data;
        this.total = total;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
