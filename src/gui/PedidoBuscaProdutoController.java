package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.ProdutoServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PedidoBuscaProdutoController implements Initializable {

    private ProdutoServico produtoServico;
    private FilteredList<Produto> produtoFilteredList;

    @FXML
    private TableView<Produto> tbvListaProduto;
    @FXML
    private TableColumn<String, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Produto, Fornecedor> tbcProdutoFornecedor;

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcProdutoFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
    }

    public void updateFormData(FilteredList<Produto> filteredList){

        tbvListaProduto.setItems(filteredList);
        tbvListaProduto.refresh();
        tbvListaProduto.getSelectionModel().selectFirst();

    }
}
