package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

// build config : --module-path H:\Noot_DELL\Dev_Software\WorkSpaceJAVA\Libs-eclipse\javafx-sdk\lib
// --add-modules=javafx.fxml,javafx.controls

//Ref. https://github.com/acenelio/workshop-javafx-jdbc

public class Main extends Application {

	//Expoe a Scene para abrir novas telas
	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// 1- Lê/Carrega o FXML - Passar o caminho completo do FXML da VIEW/Tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));

			// 2 - Carregar o control principal com a VIEW:
			ScrollPane scrollPane = loader.load();
			// Faz com que o ScrollPane se ajuste a tela:
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			// 3 - Coloca o FXML em uma Scene
			mainScene = new Scene(scrollPane);

			// CSS de Styles:
			// mainScene.getStylesheets().add("/application/application.css");

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
	
	//Retorna a Scene principal:
	public static Scene getMainScene()
	{
		return mainScene;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
