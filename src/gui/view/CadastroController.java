package gui.view;

import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.*;
import model.services.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class CadastroController implements Initializable {

    private Endereco endereco;// = new Endereco();
    private Estabelecimento estabelecimento;// = new Estabelecimento();
    private Categoria categoria;// = new Categoria();
    private Fornecedor fornecedor;// = new Fornecedor();
    private Cliente cliente;// = new Cliente();
    private Produto produto;// = new Produto();

    @FXML
    private Button btnCategoria;
    @FXML
    private Button btnCliente;
    @FXML
    private Button btnEndereco;
    @FXML
    private Button btnEstabelecimento;
    @FXML
    private Button btnFornededor;
    @FXML
    private Button btnProduto;

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void onBtnCategoriaAction(ActionEvent event){

        Stage parentStage = Utils.atualStage(event);

        carregaDialogVBox(parentStage, "/gui/view/CategoriaListDialog.fxml", (CategoriaListDialogController controller) ->{
            controller.setCategoriaServico(new CategoriaServico());
            controller.updateFormData();
        });
        /*
        carregaDialog(parentStage, "/gui/view/CategoriaCadastro.fxml", (CategoriaCadastroController controller)->{
            controller.setCategoriaServico(new CategoriaServico());
        });

         */
    }

    public void onBtnClienteAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialogVBox(parentStage, "/gui/view/ClienteCadastro.fxml", (ClienteCadastroController controller) ->{
            controller.setServico(new ClienteServico());
            controller.setCliente(cliente);
            controller.updateFormData();
        });
    }

    public void onBtnEnderecoAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/view/EnderecoCadastro.fxml", (EnderecoCadastroController controller)->{
            controller.setEndereco(endereco);
            controller.setServico(new EnderecoServico());
            controller.updateDataForm();
        });
    }

    public void onBtnEstabelecimentoAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialogVBox(parentStage, "/gui/view/EstabelecimentoCadastro.fxml", (EstabelecimentoCadastroController controller) ->{

            controller.setServico(new EstabelecimentoServico());

        });
    }

    public void onBtnFornecedorAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialogVBox(parentStage, "/gui/view/FornecedorListDialog.fxml", (FornecedorListDialogController controller) ->{
            controller.setFornecedorServico(new FornecedorServico());
            controller.updateFormData();
        });


        /*carregaDialog(parentStage, "/gui/view/FornecedorCadastro.fxml", (FornecedorCadastroController controller) ->{
            controller.setServico(new FornecedorServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.setFornecedor(fornecedor);
            controller.updateFormData();
        });
         */
    }

    public void onBtnProdutoActio(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialogVBox(parentStage, "/gui/view/ProdutoCadastro.fxml", (ProdutoCadastroController controller)->{
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.setCategoriaServico(new CategoriaServico());
            controller.setFornecedorServico(new FornecedorServico());
            controller.setProdutoServico(new ProdutoServico());
            controller.setProduto(produto);

            controller.updateFormData();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public synchronized <T> void carregaDialog(Stage parentStage, String caminho, Consumer<T> init){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            TitledPane pane = loader.load();
            Stage dialog = new Stage();

            T controller = loader.getController();
            init.accept(controller);

            dialog.setTitle("Cadastro");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized <T> void carregaDialogVBox(Stage parentStage, String caminho, Consumer<T> init){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            VBox pane = loader.load();
            Stage dialog = new Stage();

            T controller = loader.getController();
            init.accept(controller);

            dialog.setTitle("Cadastro");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
