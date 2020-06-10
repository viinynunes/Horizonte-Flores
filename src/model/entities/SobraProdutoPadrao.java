package model.entities;

public class SobraProdutoPadrao {

    private Integer id;
    private Produto produto;

    public SobraProdutoPadrao (){

    }

    public SobraProdutoPadrao(Integer id, Produto produto) {
        this.id = id;
        this.produto = produto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return produto.getNome();
    }
}
