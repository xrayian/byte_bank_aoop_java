module com.kernelcrash.byte_bank {
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.kernelcrash.byte_bank to javafx.fxml;
    exports com.kernelcrash.byte_bank;
}