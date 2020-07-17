/**
 * @author Andre
 * 
 * @date 13/07/2020 10:10:11
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
		// loadView("/gui/DepartmentList.fxml");

		// Aula sobre carregamento da tebleview com a observable list a partir do serviço:
		loadView2("/gui/DepartmentList.fxml");
	}

	@FXML
	public void onMenuItemAbutAction()
	{
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}

	private synchronized void loadView(String absoluteName)
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// Metodo temporario, sera ajustado, criado para teste do serviço de carregar a teble view
	private synchronized void loadView2(String absoluteName)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//====================================================================================
			//CARREGAR OS DADOS NA TABLEVIEW
			//====================================================================================
			// Carregar os dados na tableview --pega o controller da List e usa o serviço
			DepartmentListController controller = loader.getController();

			// usa a injeção da dependencia criado no DepartmentListController:
			controller.setDepartmentService(new DepartmentService());

			// com isso tem acesso ao metodo para carregar a listagem:
			controller.updateTableView();
		}
		catch (IOException e)
		{
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
