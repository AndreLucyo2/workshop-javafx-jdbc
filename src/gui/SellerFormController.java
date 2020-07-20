package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

// or form tem dupla função, adicionar e atualizar
public class SellerFormController implements Initializable {

	private Seller entity;

	// Injeta a dependencia do serviço do SelleroService
	private SellerService service;

	// faz injeção de dependencia para pegar a lista dos department
	private DepartmentService departmentService;

	// é a classe que emite o evento.
	// Instancia uma lista vazia que vai receber os obj ouvintes
	// Guarda uma lista de objetos interessados em receber o evento (Ouvinte)
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// Lista para o bombo Department
	private ObservableList<Department> obsList;

	// Injeção de depencia: seta a entidade do departamento
	public void setSeller(Seller entity)
	{
		// O controller vai ter uma instancia do entities departament
		this.entity = entity;
	}

	// Injeção de dependencia do serviço, e da lista de departamentos
	public void setServices(SellerService service, DepartmentService departmentService)
	{
		// Injetos o serviço, para pegar os serviços
		this.service = service;

		// Injeta depatament, para carregar o combo
		this.departmentService = departmentService;
	}

	// permite outros betos se inscreverem na lista pada poder ouvir/ receber os eventos
	public void subscribeDataChangeListener(DataChangeListener listener)
	{
		// inscreve os interessados em ouvir os eventos quando algo mudar
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event)
	{

		// Como foi feito a injeção manualmente, validar se nao esta null
		if (entity == null)
		{
			throw new IllegalStateException("Entity was null");
		}

		// Como foi feito a injeção manualmente, validar se nao esta null
		if (service == null)
		{
			throw new IllegalStateException("Service was null");
		}

		// Como lida com banco, pode gerar db exception, usar try
		try
		{
			// Pega os dados da tela: tanto para salvar quanto para alterar
			entity = getFormData();

			// Valida se é insert ou update, e grava / cria no banco
			service.saveOrUpdate(entity);

			// ==========================================================================
			// Evento que notifica os ouvintes em caso de salvamento com sucesso
			notifyDataChangeListeners();

			// Manda fechar a janela atual, pega a referencia do evento
			Utils.currentStage(event).close();

		}
		catch (ValidationException e)// implementa a listagem dos erros
		{
			// caso acontece um erro de validação:
			setErrorMessages(e.getErrors());
		}
		catch (DbException e)
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// Implementação da classe que emite os eventos
	// Emite o evento para todos os ouvintes ver Seção 23 aula 282
	private void notifyDataChangeListeners()
	{
		// para cada DataChangeListener da minha lista dataChangeListeners faça ... onDataChanged
		for (DataChangeListener listener : dataChangeListeners)
		{
			// Emite um evendo a cada ciclo:
			listener.onDataChanged();
		}
	}

	// Passar os dados da tela para uma instnacia do objeto obj
	// implamenta as validações e lanca exceptions caso der erro
	private Seller getFormData()
	{
		// Instancia um objeto:
		Seller obj = new Seller();

		// instancia uma exception de validação, porem ainda nao foi lançada
		ValidationException exception = new ValidationException("Validation error");

		// Pega os dados da tela
		// Pega string ja convertendo para int, caos null retorna null
		obj.setId(Utils.tryParseToInt(txtId.getText()));

		// CAMPO NOME:--------------------------------------------------------------
		// Validação: o campo nome nao pode ser vazio:
		if (txtName.getText() == null || txtName.getText().trim().equals(""))
		{
			// Adiciona uma mensagem de erro na exceção
			//ATENÇÃO! a chave deve ser exatamente igual ao nome do campo da entidade
			exception.addError("name", "o Campo nao pode ser vazio!!");
		}
		// pegar o nome da tela:
		obj.setName(txtName.getText());

		// CAMPO EMAIL:---------------------------------------------------------------
		// Teste se é nulo ou vazio:
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals(""))
		{
			// Adiciona uma mensagem de erro na exceção
			//ATENÇÃO! a chave deve ser exatamente igual ao nome do campo da entidade
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());

		// CAMPO BIRTHDATE:-----------------------------------------------------------
		// Teste se é nulo ou vazio:
		if (dpBirthDate.getValue() == null)
		{
			
			// Adiciona uma mensagem de erro na exceção
			//ATENÇÃO! a chave deve ser exatamente igual ao nome do campo da entidade
			exception.addError("birthDate", "Field can't be empty");
		}
		else
		{
			// recebe o conteudo do datepicker - converte a data com horario do pc, para uma data
			// independente da região, pois no banco fica a data sem região
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			// passa para o campo:
			obj.setBirthDate(Date.from(instant));
		}

		// CAMPO BASE SALARY:--------------------------------------------------------------
		// Teste se é nulo ou vazio:
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals(""))
		{
			// Adiciona uma mensagem de erro na exceção
			//ATENÇÃO! a chave deve ser exatamente igual ao nome do campo da entidade
			exception.addError("baseSalary", "Field can't be empty");
		}
		// tem que converte de Strind para double, se preencher errado vai ficar como null
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

		//Pega o departament do Combobox
		obj.setDepartment(comboBoxDepartment.getValue());
		
		// Testa se a exceção pegando os erros for maior que zero
		// tese se na coleção tem pelo menos um erro
		// lana a exceção caso tenha algum erro
		if (exception.getErrors().size() > 0)
		{
			// Se o tamanho dos erros for maior que zero, lança execção
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		// fecha a tela, pegando o stage do botão/Event, clicado
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// carregar as restrições:
		initializeNodes();
	}

	// definira as restrições dos campos:
	// carregas e definir os nodos = controles da tela
	private void initializeNodes()
	{
		// Campo id so aceita inteiros
		Constraints.setTextFieldInteger(txtId);
		// Campo nome tem comprimento definido
		Constraints.setTextFieldMaxLength(txtName, 70);
		// definir campo do tipo double:
		Constraints.setTextFieldDouble(txtBaseSalary);
		// Definir o tamnho maximo do campo texto
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		// Formata a data usando classe Utils: formata a data que vem do DatePicker
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

		// Carregar o controle Combobox
		initializeComboBoxDepartment();

	}

	// pega a entidade e popula as caixinhas da tela
	// Metodo que pega os dados dos objetos e preenche os campos na tela
	public void updateFormData()
	{
		// testa se nao esta null: Prgramação de fenciva
		if (entity == null)
		{
			// lança uma exceção pois nao foi carregado um department
			throw new IllegalStateException("Entity was null");
		}

		// Popular os campos:
		// converter para string
		txtId.setText(String.valueOf(entity.getId()));

		// carrega o dado name:
		txtName.setText(entity.getName());

		// carrega o dado email:
		txtEmail.setText(entity.getEmail());

		// mada por o ponto e nao a virgula
		Locale.setDefault(Locale.US);
		// carrega o dado velo, convertendo double para String:
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));

		// a conversao nao aceita valour null, dessa forma faz um teste antes
		// so converte se nao tiver nulo
		if (entity.getBirthDate() != null)
		{
			// no banco a data esta grava independente da localidade, porem na tela
			// deve ser mostrada levando em consideração o local, pegando o fuzorario do pc onde esta rodando a
			// aplicação
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

		// COMBOBOX:
		// passa os dados para o combobox
		// Testa se nao tem dados, no caso de ser o primeiro dado
		if (entity.getDepartment() == null)
		{
			// é um vendedor novo, ele nao tem departamnto ainda
			// Manda seleciona o primeir elemento do combobox
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else
		{
			// Caso ja tenha um departamento associado, carrega ele no combo
			// no aso de edição de um registro
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	// Carregar os ojetos assiciados a tela:
	// Carregar o combobox
	public void loadAssociatedObjects()
	{
		// Valida se nao foi feito a injeção de dependencia la na hora de carregar a tela
		if (departmentService == null)
		{
			throw new IllegalStateException("DepartmentService was null");
		}

		// Carrega os departamentos do banco
		List<Department> list = departmentService.findAll();
		// Passa a lista retornada para a lista:
		obsList = FXCollections.observableArrayList(list);
		// Passa a list para o Combobox
		comboBoxDepartment.setItems(obsList);
	}

	// Passar as mensagens de erro para o label da tela
	// metodo responsável por pegar as mensagens de erro e mostrar na tela
	// Recebe uma coleção de erros.
	private void setErrorMessages(Map<String, String> errors)
	{
		// cria um conjunto de campos
		Set<String> fields = errors.keySet();

		// Adiciona uma mensagem de erro na exceção
		//ATENÇÃO! no if deve ser exatamente igual ao nome do campo da entidade		
		//Mostra erro no campo nome no label da tela:
		// percorre a lista de campos, percorre a lista, buscanso pelo nome do campo
		// busca na coleção se possui alguma mensagem com o nome do campo
		if (fields.contains("name"))
		{
			// Mostra o erro em uma label na tela , para exemplo
			labelErrorName.setText(errors.get("name"));
		}
		else
		{
			//Limpar o erro:
			labelErrorName.setText(errors.get(""));
		}

		// O if pode ser feito de forma forma TERNARIA:
		//Mostra erro no campo nome no label, caso nao tem erro, o label fica vazio
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		
		//Mostra erro no campo nome no label, caso nao tem erro, o label fica vazio
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		
		//Mostra erro no campo nome no label, caso nao tem erro, o label fica vazio
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));

	}

	// Metodo que carrega o combobox e atualiza
	private void initializeComboBoxDepartment()
	{
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>()
		{
			@Override
			protected void updateItem(Department item, boolean empty)
			{
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
