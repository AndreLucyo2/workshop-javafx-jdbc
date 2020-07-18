package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

// or form tem dupla função, adicionar e atualizar
public class DepartmentFormController implements Initializable {

	private Department entity;

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

	@FXML
	public void onBtSaveAction()
	{
		System.out.println("onBtSaveAction");
	}

	@FXML
	public void onBtCancelAction()
	{
		System.out.println("onBtCancelAction");
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

	//pega a entidade e popula as caixinhas da tela
	public void updateFormData()
	{
		//testa se nao esta null: Prgramação de fenciva
		if (entity == null)
		{
			//lança uma exceção pois nao foi carregado um department
			throw new IllegalStateException("Entity was null");
		}
		
		//Popular os campos:
		//converter para string
		txtId.setText(String.valueOf(entity.getId()));
		
		//carrega o dado name:
		txtName.setText(entity.getName());
	}
}
