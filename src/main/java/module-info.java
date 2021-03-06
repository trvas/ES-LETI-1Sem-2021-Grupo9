module es.grupo9 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires trello4j;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires github.api;
    requires markdown4j;
    requires okhttp3;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;

    opens es.grupo9 to javafx.fxml;
    exports es.grupo9;
}