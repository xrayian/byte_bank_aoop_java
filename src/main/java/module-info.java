module com.kernelcrash.byte_bank {
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    //icon packs
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.net.http;


    opens com.kernelcrash.byte_bank to javafx.fxml;
    opens com.kernelcrash.byte_bank.controllers.dashboard to javafx.fxml;
    opens com.kernelcrash.byte_bank.controllers.onboarding to javafx.fxml;
    exports com.kernelcrash.byte_bank;
    exports com.kernelcrash.byte_bank.controllers.dashboard;
    exports com.kernelcrash.byte_bank.controllers.onboarding;
}