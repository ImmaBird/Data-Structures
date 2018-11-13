package train;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.stage.Stage;

public class GUI extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	// Starts the GUI
	@Override
	public void start(Stage stage) throws Exception {
		ScrollPane pane = new ScrollPane(new Simulation());
		pane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		pane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		pane.setPrefSize(800, 600);
		Scene root = new Scene(pane);
		stage.setTitle("Train Simulation");
		stage.setScene(root);
		stage.show();
		stage.setFullScreen(true);
	}

}
