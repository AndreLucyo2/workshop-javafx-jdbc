/**
 * @author Andre
 * 
 * @date 14/07/2020 10:58:49
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	// Injeção de dependencia: ver setDepartmentService
	private DepartmentService service;

	//
	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction()
	{
		System.out.println("onBtNewAction");
	}

	// Injeção de dependencia: é um padrão solid, melhor em vez de dar um new service.
	public void setDepartmentService(DepartmentService service)
	{
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		initializeNodes();
	}

	private void initializeNodes()
	{
		// Inicia o comportament das colunas:
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Fazer a Table acompanhar a janela: Macete
		Stage stage = (Stage) Main.getMainScene().getWindow();
		//Controla o teamaha da table view conforme ajusta a tela:
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}

	// acessa o serviço, carregar os departamentos e joga na lista
	public void updateTableView()
	{
		// Se o serviço nao foi instanciado:
		if (service == null)
		{
			// se o serviço estiver nullo, caso esquecer de injetar a dependencia
			throw new IllegalStateException("Service was null");
		}

		// Cria uma lista: o service retorna a lista ja com dados
		List<Department> list = service.findAll();

		// Passa para a observable list:
		obsList = FXCollections.observableArrayList(list);

		// Carrega a observablelist na table view
		tableViewDepartment.setItems(obsList);
	}

}
