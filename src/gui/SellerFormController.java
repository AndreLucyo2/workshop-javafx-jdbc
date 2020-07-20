package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

// or form tem dupla fun��o, adicionar e atualizar
public class SellerFormController implements Initializable {

	private Seller entity;

	// Injeta a dependencia do servi�o do SelleroService
	private SellerService service;

	// � a classe que emite o evento.
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

	// Inje��o de depencia: seta a entidade do departamento
	public void setSeller(Seller entity)
	{
		// O controller vai ter uma instancia do entities departament
		this.entity = entity;
	}

	// Inje��o de dependencia do servi�o
	public void setSellerService(SellerService service)
	{
		this.service = service;
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

		// Como foi feito a inje��o manualmente, validar se nao esta null
		if (entity == null)
		{
			throw new IllegalStateException("Entity was null");
		}

		// Como foi feito a inje��o manualmente, validar se nao esta null
		if (service == null)
		{
			throw new IllegalStateException("Service was null");
		}

		// Como lida com banco, pode gerar db exception, usar try
		try
		{
			// Pega os dados da tela: tanto para salvar quanto para alterar
			entity = getFormData();

			// Valida se � insert ou update, e grava / cria no banco
			service.saveOrUpdate(entity);

			// ==========================================================================
			// Evento que notifica os ouvintes em caso de salvamento com sucesso
			notifyDataChangeListeners();

			// Manda fechar a janela atual, pega a referencia do evento
			Utils.currentStage(event).close();

		}
		catch (ValidationException e)// implementa a listagem dos erros
		{
			// caso acontece um erro de valida��o:
			setErrorMessages(e.getErrors());
		}
		catch (DbException e)
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// Implementa��o da classe que emite os eventos
	// Emite o evento para todos os ouvintes ver Se��o 23 aula 282
	private void notifyDataChangeListeners()
	{
		// para cada DataChangeListener da minha lista dataChangeListeners fa�a ... onDataChanged
		for (DataChangeListener listener : dataChangeListeners)
		{
			// Emite um evendo a cada ciclo:
			listener.onDataChanged();
		}
	}

	// Passar os dados da tela para uma instnacia do objeto obj
	// implamenta as valida��es e lanca exceptions caso der erro
	private Seller getFormData()
	{
		// Instancia um objeto:
		Seller obj = new Seller();

		// instancia uma exception de valida��o, porem ainda nao foi lan�ada
		ValidationException exception = new ValidationException("Validation error");

		// Pega os dados da tela
		// Pega string ja convertendo para int, caos null retorna null
		obj.setId(Utils.tryParseToInt(txtId.getText()));

		// Valida��o: o campo nome nao pode ser vazio:
		if (txtName.getText() == null || txtName.getText().trim().equals(""))
		{
			// Adiciona uma mensagem de erro na exce��o
			exception.addError("name", "o Campo nao pode ser vazio!!");
		}

		// pegar o nome da tela:
		obj.setName(txtName.getText());

		// Testa se a exce��o pegando os erros for maior que zero
		// tese se na cole��o tem pelo menos um erro
		// lana a exce��o caso tenha algum erro
		if (exception.getErrors().size() > 0)
		{
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		// fecha a tela, pegando o stage do bot�o/Event, clicado
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// carregar as restri��es:
		initializeNodes();
	}

	// definira as restri��es dos campos:
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

	}

	// pega a entidade e popula as caixinhas da tela
	public void updateFormData()
	{
		// testa se nao esta null: Prgrama��o de fenciva
		if (entity == null)
		{
			// lan�a uma exce��o pois nao foi carregado um department
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
		
		//a conversao nao aceita valour null, dessa forma faz um teste antes
		//so converte se nao tiver nulo
		if (entity.getBirthDate() != null)
		{
			// no banco a data esta grava independente da localidade, porem na tela
			// deve ser mostrada levando em considera��o o local, pegando o fuzorario do pc onde esta rodando a aplica��o
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}

	// metodo respons�vel por pegar as mensagens de erro e mostrar na tela
	// Recebe uma cole��o de erros.
	private void setErrorMessages(Map<String, String> errors)
	{
		// cria um conjunto de campos
		Set<String> fields = errors.keySet();

		// percorre a lista de campos, percorre a lista, buscanso pelo nome do campo
		// busca na cole��o se possui alguma mensagem com o nome do campo
		if (fields.contains("name"))
		{
			// Mostra o erro em uma label na tela , para exemplo
			labelErrorName.setText(errors.get("name"));
		}
	}
}
