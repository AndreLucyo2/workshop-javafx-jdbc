package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//build config :   --module-path H:\Noot_DELL\Dev_Software\WorkSpaceJAVA\Libs-eclipse\javafx-sdk\lib --add-modules=javafx.fxml,javafx.controls

public class Main extends Application {

	@Override
	public void start(Stage stage)
	{
		try
		{
			// 1- Lê/Carrega o FXML - Passar o caminho completo do FXML
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
			
			// 2 - Coloca o FXML em uma Scene
			Scene scene = new Scene(parent);

			// CSS de Styles:
			//scene.getStylesheets().add("/application/application.css");

			// 3 - Coloca a Scene em uma janela
			stage.setScene(scene);

			// 4 - Mostra a tela
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
