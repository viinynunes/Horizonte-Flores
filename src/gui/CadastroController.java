package gui;

import gui.controller.*;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Cliente;
import model.entities.Produto;
import model.services.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class CadastroController implements Initializable {

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

    public void onBtnCategoriaAction(ActionEvent event){

        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/CategoriaCadastro.fxml", (CategoriaCadastroController controller)->{
            controller.setCategoriaServico(new CategoriaServico());
        });
    }

    public void onBtnClienteAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        Cliente cliente = new Cliente();
        carregaDialogVBox(parentStage, "/gui/ClienteCadastro.fxml", (ClienteCadastroController controller) ->{
            controller.setServico(new ClienteServico());
            controller.setCliente(cliente);
            controller.updateFormData();
        });
    }

    public void onBtnEnderecoAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/EnderecoCadastro.fxml", (EnderecoCadastroController controller)->{});
    }

    public void onBtnEstabelecimentoAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialogVBox(parentStage, "/gui/EstabelecimentoCadastro.fxml", (EstabelecimentoCadastroController controller) ->{
        });
    }

    public void onBtnFornecedorAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/FornecedorCadastro.fxml", (FornecedorCadastroController controller) ->{});
    }

    public void onBtnProdutoActio(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        Produto produto = new Produto();
        carregaDialogVBox(parentStage, "/gui/ProdutoCadastro.fxml", (ProdutoCadastroController controller)->{
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.setCategoriaServico(new CategoriaServico());
            controller.setFornecedorServico(new FornecedorServico());
            controller.setProduto(produto);
            controller.setProdutoServico(new ProdutoServico());
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
