package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class TransferConversionController {
    @FXML
    ChoiceBox<String> payWalletSelBox;

    @FXML
    TextField payAmountField;

    @FXML
    ChoiceBox<String> receiveWalletSelBox;

    @FXML
    TextField receiveAmountValueField;

    @FXML
    public ChoiceBox<String> trxWalletSelBox;

    @FXML
    public TextField trxAmountField;

    @FXML
    public TextField accountNameField;

    @FXML
    Button transferBtn;

    @FXML
    Button convertBtn;

    @FXML
    void initialize() {
        setupUI();

    }

    private void setupUI() {
        MainApplication.stateManager.getCurrentUser().getWallets().forEach(wallet -> {
            payWalletSelBox.getItems().add(wallet.getWalletName() + " (" + wallet.getUIFriendlyBalance() + " " + wallet.getCryptoType() + ")");
            receiveWalletSelBox.getItems().add(wallet.getWalletName() + " ("+wallet.getCryptoType() + ")");
            trxWalletSelBox.getItems().add(wallet.getWalletName() + " (" + wallet.getUIFriendlyBalance() + " " + wallet.getCryptoType() + ")");
        });
    }


}
