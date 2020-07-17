/**
 * @author Andre
 * 
 * @date 13/07/2020 10:10:11
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewCoontroller implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction()
	{
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction()
	{
		//Primeira versao, sem carregamento:
		// loadView("/gui/DepartmentList.fxml");

		// ====================================================================================
		// LoadView com função de CARREGAR OS DADOS NA TABLEVIEW
		// ====================================================================================
		// Load view com uma função de inicialização:
		// usa uma função lambda, esta fução faz o carregamento
		// da tableview, DepartmentListController o consumer<T> retorna o controller da classe informada
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) ->
		{

			// Carregar os dados na tableview --pega o controller da List e usa o serviço
			controller.setDepartmentService(new DepartmentService());

			// usa a injeção da dependencia criado no DepartmentListController:
			// com isso tem acesso ao metodo para carregar a listagem:
			controller.updateTableView();

		});
	}

	@FXML
	public void onMenuItemAbutAction()
	{
		// como nao usa parametro, poe uma lambda que nao faz nada,
		loadView("/gui/About.fxml", x ->
		{
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}

	//usa uma função como parametro, usa lambda e generics consumer<T> de um tipo qualquer e inicializavel
	//adicona o comando para executar a função:
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction)
	{
		try
		{
			// Criar um loader
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			// Recebe um VBox do parametro
			VBox newVBox = loader.load();

			// Recebe uma Scena do Main
			Scene mainScene = Main.getMainScene();

			// Navega e guarda a VBox do Main:
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// Guarda o primeiro filho da VBox do Main: o menu itens
			Node mainMenu = mainVBox.getChildren().get(0);

			// Limpa os filhos da VBoxMain
			mainVBox.getChildren().clear();

			// Adiciona novamente o menuItens:
			mainVBox.getChildren().add(mainMenu);

			// Adiciona todos os filhos da nova tela:
			mainVBox.getChildren().addAll(newVBox.getChildren());

			// ====================================================================================
			// CARREGAR OS DADOS NA TABLEVIEW - função como argumento
			// ====================================================================================
			//pega o controller da List e usa o serviço, retorna um controller do tipo T
			//estas duas linhas executam a função que for passada como argumento
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
