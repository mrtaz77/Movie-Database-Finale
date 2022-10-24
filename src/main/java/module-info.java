module system.moviedatabasefinale {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens client to javafx.graphics,javafx.fxml;
    opens controllers to javafx.graphics,javafx.fxml;
}