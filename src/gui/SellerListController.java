/**
 * @author Andre
 * 
 * @date 14/07/2020 10:58:49
 */
package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

// implementa a interface DataChangeListener-- fica ouvindo eventos
public class SellerListController implements Initializable, DataChangeListener {

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	// Coluna com ação de edição
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	// Coluna com ação de deletar
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	// Injeção de dependencia: ver setSellerService
	private SellerService service;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event)// para cadastrar um novo department
	{
		// pega a referencia para o stage atual
		Stage parentStage = gui.util.Utils.currentStage(event);

		// Instancia um objeto vazio: ação de criar uma novo cadastro
		Seller obj = new Seller();

		// carregar a tela passando:
		// -objeto: vazio para novo
		// -a view FXML
		// -o stage atual:
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	// Injeção de dependencia: é um padrão solid, melhor em vez de dar um new service.
	public void setSellerService(SellerService service)
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
		// Inicia o comportament das colunas: deve ser o mesmo nome do campo na classe entidade
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		//Coluna do tipo data
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		//Para que a data apareça formatada , aplica o metodo em Utils
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		//Coluna do tipo numero com casas decimais
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		//Para que a data apareça formatada , aplica o metodo em Utils
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);		

		// Fazer a Table acompanhar a janela: Macete
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// Controla o teamaha da table view conforme ajusta a tela:
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

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
		List<Seller> list = service.findAll();

		// Passa para a observable list:
		obsList = FXCollections.observableArrayList(list);

		// Carrega a observablelist na table view
		tableViewSeller.setItems(obsList);

		// vai criar um botão de edição em cada linha que tiver dados
		// Ao clicar abre a tela de edição:
		initEditButtons();
		
		// vai criar um botão para remover em cada linha que tiver dados
		initRemoveButtons();
	}

	// Ao criar uma janela, sempre precisa informar o stage que criou a janela de dialogo,
	// precisa passar o caminho aboluot da janela
	// injeta um objeto departament na view SellerForm
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage)
	{
//		try
//		{
//			// Logica para abrir um janela de dialogo:
//			// Criar um loader
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			// Recebe um Panel do parametro: Carregou a view
//			Pane pane = loader.load();
//
//			// =============================================================================
//			// injeta o departa no controller na view do formulario
//			// Pega o controller da tela recebida pelo parametro
//			SellerFormController controller = loader.getController();
//
//			// Injeta o objeto no controller
//			controller.setSeller(obj);
//
//			// Injeção de dependencia do service:
//			controller.setSellerService(new SellerService());
//
//			// Padraão de programação observer: é avançado, alto desacoplamento, o objeto que emite o evento nao
//			// conhece que esta escutando
//			// Se inscever para receber o evento observado
//			// Quando o evento for disparado pela classe sera executado o onDataChanged
//			controller.subscribeDataChangeListener(this);
//
//			// Chama o metodo que carrega os dados na tela:
//			controller.updateFormData();
//
//			// =============================================================================
//			// CRiar um novo stage para a janela modal, um pauco na frente do outro:
//			Stage dialogStage = new Stage();
//			// Titulo da tela:
//			dialogStage.setTitle("Enter Seller data");
//			// Definir a cena:
//			dialogStage.setScene(new Scene(pane));
//			// nao deixa redimencionar:
//			dialogStage.setResizable(false);
//			// quem é o stage pai desta janela: pega do parametro
//			dialogStage.initOwner(parentStage);
//			// definir se sera modal: bloqueia a janela anterior:
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			// Mostra a janela:
//			dialogStage.showAndWait();
//		}
//		catch (IOException e)
//		{
//
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged()
	{
		// Seção 23 - implementando o Listner, classe que se increve e fica ouvindo os eventos de outro
		// objeto
		// Atualiza os dados da tela:
		updateTableView();

	}

	// Metodo que cria uma botão Edit, e ja configura um evento para o botão
	// Ver material de apoio seção 23 - Update department
	private void initEditButtons()
	{
		// Coluna de edição: metodo especifico para javaFX
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>()
		{
			// Cria o botao: edit
			private final Button button = new Button("Editar");

			// Carrega o objeto da linha que tiver clicado
			@Override
			protected void updateItem(Seller obj, boolean empty)
			{
				super.updateItem(obj, empty);

				// Testa se o objeto nao esta null
				if (obj == null)
				{
					setGraphic(null);
					return;
				}

				// Carregar a tela de edição -usando lambda:
				setGraphic(button);
				button.setOnAction(
				event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// Metodo que cria uma botão Remove, e ja configura um evento para o botão
	// Ver material de apoio seção 23 - Remove department
	private void initRemoveButtons()
	{
		// Coluna de remove: metodo especifico para javaFX
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>()
		{
			// Cria o botao: edit
			private final Button button = new Button("remove");

			// Carrega o objeto da linha que tiver clicado
			@Override
			protected void updateItem(Seller obj, boolean empty)
			{
				super.updateItem(obj, empty);
				
				// Testa se o objeto nao esta null
				if (obj == null)
				{
					setGraphic(null);
					return;
				}
				
				// Define a ação do botão - usando lambda: Executa metodo removeEntity
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	//
	private void removeEntity(Seller obj)
	{
		//Mostra o Alert para confirmar a deleção, pega o resulstado do Alert = botão clicado
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que voce quer deletar?");

		//verificar o que foi clicado, se foi clicado OK, confirmou a deleção
		if (result.get() == ButtonType.OK)
		{
			//Testa se foi injetado o serviço
			if (service == null)
			{
				throw new IllegalStateException("Service was null");
			}
			
			//Vaida se resulta alguma exceptin de integridade
			try
			{
				//Faz o remove:
				service.remove(obj);
				
				//Atualiza a tableView
				updateTableView();
			}
			catch (DbIntegrityException e)
			{
				//Alerta de erro ao deletar. exemplo: item esta em uso .. etc.
				Alerts.showAlert("Error removing object! \n este item esta em uso nao pode ser removido", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
