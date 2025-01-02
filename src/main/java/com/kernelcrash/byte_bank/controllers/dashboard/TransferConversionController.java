package com.kernelcrash.byte_bank.controllers.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class TransferConversionController {
    @FXML
    ChoiceBox<String> payCryptoBox;

    @FXML
    ChoiceBox<String> receiveCryptoBox;

    @FXML
    void initialize() {
        // Define the options for the ChoiceBoxes
        ObservableList<String> cryptoOptions = FXCollections.observableArrayList(
                "Bitcoin (BTC)",
                "Ethereum (ETH)",
                "Litecoin (LTC)",
                "Cardano (ADA)"
        );

        // Add options to the ChoiceBoxes
        payCryptoBox.setItems(cryptoOptions);
        receiveCryptoBox.setItems(cryptoOptions);

        // Set default values (optional)
        payCryptoBox.setValue("Bitcoin (BTC)");
        receiveCryptoBox.setValue("Ethereum (ETH)");
    }


}
