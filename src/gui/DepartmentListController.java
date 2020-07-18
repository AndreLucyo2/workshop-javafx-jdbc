/**
 * @author Andre
 * 
 * @date 14/07/2020 10:58:49
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

//implementa a interface DataChangeListener-- fica ouvindo eventos
public class DepartmentListController implements Initializable, DataChangeListener {

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	// Inje��o de dependencia: ver setDepartmentService
	private DepartmentService service;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event)// para cadastrar um novo department
	{
		// pega a referencia para o stage atual
		Stage parentStage = gui.util.Utils.currentStage(event);

		// Instancia um objeto vazio: a��o de criar uma novo cadastro
		Department obj = new Department();

		// carregar a tela passando:
		// -objeto: vazio para novo
		// -a view FXML
		// -o stage atual:
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// Inje��o de dependencia: � um padr�o solid, melhor em vez de dar um new service.
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
		// Controla o teamaha da table view conforme ajusta a tela:
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}

	// acessa o servi�o, carregar os departamentos e joga na lista
	public void updateTableView()
	{
		// Se o servi�o nao foi instanciado:
		if (service == null)
		{
			// se o servi�o estiver nullo, caso esquecer de injetar a dependencia
			throw new IllegalStateException("Service was null");
		}

		// Cria uma lista: o service retorna a lista ja com dados
		List<Department> list = service.findAll();

		// Passa para a observable list:
		obsList = FXCollections.observableArrayList(list);

		// Carrega a observablelist na table view
		tableViewDepartment.setItems(obsList);
	}

	// Ao criar uma janela, sempre precisa informar o stage que criou a janela de dialogo,
	// precisa passar o caminho aboluot da janela
	// injeta um objeto departament na view DepartmentForm
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage)
	{
		try
		{
			// Logica para abrir um janela de dialogo:
			// Criar um loader
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// Recebe um Panel do parametro: Carregou a view
			Pane pane = loader.load();

			// =============================================================================
			// injeta o departa no controller na view do formulario
			// Pega o controller da tela recebida pelo parametro
			DepartmentFormController controller = loader.getController();
			
			// Injeta o objeto no controller
			controller.setDepartment(obj);
			
			// Inje��o de dependencia do service:
			controller.setDepartmentService(new DepartmentService());
			
			//Padra�o de programa��o observer: � avan�ado, alto desacoplamento, o objeto que emite o evento nao conhece que esta escutando
			//Se inscever para receber o evento observado
			//Quando o evento for disparado pela classe  sera executado o onDataChanged			
			controller.subscribeDataChangeListener(this);
			
			// Chama o metodo que carrega os dados na tela:
			controller.updateFormData();

			// =============================================================================
			// CRiar um novo stage para a janela modal, um pauco na frente do outro:
			Stage dialogStage = new Stage();
			// Titulo da tela:
			dialogStage.setTitle("Enter Department data");
			// Definir a cena:
			dialogStage.setScene(new Scene(pane));
			// nao deixa redimencionar:
			dialogStage.setResizable(false);
			// quem � o stage pai desta janela: pega do parametro
			dialogStage.initOwner(parentStage);
			// definir se sera modal: bloqueia a janela anterior:
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// Mostra a janela:
			dialogStage.showAndWait();
		}
		catch (IOException e)
		{

			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged()
	{
		//Se��o 23 - implementando o Listner, classe que se increve e fica ouvindo os eventos de outro objeto
		//Atualiza os dados da tela:
		updateTableView();
	
		
	}

}
