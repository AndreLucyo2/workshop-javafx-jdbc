package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	// Função para pegar o stage a partir de um control, ou seja a partir de onde esta - Pauco atual
	public static Stage currentStage(ActionEvent event)
	{
		//Pegar o Stage a partir de uma ação disparada por um btn ou menu...
		//a partir do node, pega a scene e da sece napra a window w retorna o stage atual
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
