package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

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
		//carregar as restrições:
		initializeNodes();
	}

	// definira as restrições dos campos:
	private void initializeNodes()
	{
		//Campo id so aceita inteiros
		Constraints.setTextFieldInteger(txtId);
		//Campo nome tem comprimento definido
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
}
