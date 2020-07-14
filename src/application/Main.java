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
	public void start(Stage primaryStage)
	{
		try
		{
			// 1- Lê/Carrega o FXML - Passar o caminho completo do FXML da VIEW/Tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			// 2 - Carregar o control Parent: Controle principal, pai de todos neste exemplo um AnchorPane		
			Parent parent = loader.load();
			
			// 3 - Coloca o FXML em uma Scene
			Scene mainScene = new Scene(parent);

			// CSS de Styles:
			//mainScene.getStylesheets().add("/application/application.css");

			// 4 - Coloca a Scene em uma janela
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");

			// 5 - Mostra a tela
			primaryStage.show();
					
			
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
