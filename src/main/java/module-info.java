module com.kernelcrash.byte_bank {
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.kernelcrash.byte_bank to javafx.fxml;
    opens com.kernelcrash.byte_bank.controller.dashboard to javafx.fxml;
    opens com.kernelcrash.byte_bank.controller.onboarding to javafx.fxml;
    exports com.kernelcrash.byte_bank;
    exports com.kernelcrash.byte_bank.controller.dashboard;
    exports com.kernelcrash.byte_bank.controller.onboarding;
}