package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

// or form tem dupla função, adicionar e atualizar
public class DepartmentFormController implements Initializable {

	private Department entity;

	// Injeta a dependencia do serviço do DepartmentoService
	private DepartmentService service;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// Injeção de depencia: seta a entidade do departamento
	public void setDepartment(Department entity)
	{
		// O controller vai ter uma instancia do entities departament
		this.entity = entity;
	}

	// Injeção de dependencia do serviço
	public void setDepartmentService(DepartmentService service)
	{
		this.service = service;
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

			// Manda fechar a janela atual, pega a referencia do evento
			Utils.currentStage(event).close();
		}
		catch (DbException e)
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// Passar os dados da tela para uma instnaci do objeto obj
	private Department getFormData()
	{
		// Instancia um objeto:
		Department obj = new Department();

		// Pega os dados da tela
		// Pega string ja convertendo para int, caos null retorna null
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		// pegar o nome da tela:
		obj.setName(txtName.getText());

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		//fecha a tela, pegando o stage do botão/Event, clicado
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// carregar as restrições:
		initializeNodes();
	}

	// definira as restrições dos campos:
	private void initializeNodes()
	{
		// Campo id so aceita inteiros
		Constraints.setTextFieldInteger(txtId);
		// Campo nome tem comprimento definido
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	// pega a entidade e popula as caixinhas da tela
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
	}
}
