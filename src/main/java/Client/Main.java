package client;

import components.ProductionCompany;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;

public class Main extends Application {
    //only 3 members needed
    private Stage stage;
    private String productionCompany;
    private NetworkUtil networkUtil;

    public Stage getStage() {
        return stage;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    //overriding start of abstract class Application
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        System.out.println("In start of client");
        connectToServer();
        System.out.println("Connected to server successfully...");
    }

    //connect to server method
    private void connectToServer() throws IOException {
        System.out.println("Trying to connect to server...");
        String serverAddress = "127.0.0.1";
        int serverPort = 44444;
        NetworkUtil networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThreadClient(this);
    }

    public static void main(String[] args) {
        System.out.println("In main of client , launching JavaFx application");
        launch(args);
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Login");
        alert.setHeaderText("Invalid Login");
        alert.setContentText("Production Company not found.");
        alert.showAndWait();
    }

    public void showMenuPage(ProductionCompany productionCompany, String option) {
    }
}
