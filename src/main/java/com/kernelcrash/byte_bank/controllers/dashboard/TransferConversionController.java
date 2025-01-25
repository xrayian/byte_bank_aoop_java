package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.Wallet;
import com.kernelcrash.byte_bank.utils.HttpClientHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TransferConversionController {
    @FXML
    ChoiceBox<Wallet> payWalletSelBox;

    @FXML
    TextField payAmountField;

    @FXML
    ChoiceBox<Wallet> receiveWalletSelBox;

    @FXML
    TextField receiveAmountValueField;

    @FXML
    public ChoiceBox<Wallet> trxWalletSelBox;

    @FXML
    public TextField trxAmountField;

    @FXML
    public TextField accountNameField;

    @FXML
    Button transferBtn;

    @FXML
    Button convertBtn;

    @FXML
    Label conversionAvlBalance;

    @FXML
    Label transferWalletBalance;

    @FXML
    void initialize() {
        setupUI();

    }

    private void setupUI() {
        // Add wallets to the choice boxes
        MainApplication.stateManager.getCurrentUser().getWallets().forEach(wallet -> {
            payWalletSelBox.getItems().add(wallet);
            receiveWalletSelBox.getItems().add(wallet);
            trxWalletSelBox.getItems().add(wallet);
        });

        payWalletSelBox.setOnAction(event -> {
            Wallet selectedWallet = payWalletSelBox.getValue();
            conversionAvlBalance.setText(selectedWallet.getUIFriendlyBalance() + " " + selectedWallet.getCryptoType());
        });

        payAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            //only allow numbers and decimals
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                payAmountField.setText(oldValue);
            }

            if (payWalletSelBox.getSelectionModel().getSelectedItem() != null) {
                Wallet selectedWallet = MainApplication.stateManager.getCurrentUser().getWallets().get(payWalletSelBox.getSelectionModel().getSelectedIndex());
                newValue = newValue.isEmpty() ? "0" : newValue;
                double amount = Double.parseDouble(newValue);
                double balance = selectedWallet.getBalance();
                if (amount > balance) {
                    payAmountField.setText(oldValue);
                }
                updateEstimatedReceiveAmountField();
            }
        });

        trxAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            //only allow numbers and decimals
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                trxAmountField.setText(oldValue);
            }

            if (trxWalletSelBox.getSelectionModel().getSelectedItem() != null) {
                Wallet selectedWallet = trxWalletSelBox.getValue();
                newValue = newValue.isEmpty() ? "0" : newValue;
                double amount = Double.parseDouble(newValue);
                double balance = selectedWallet.getBalance();
                if (amount > balance) {
                    trxAmountField.setText(oldValue);
                }
            }
        });

        updateEstimatedReceiveAmountField();

        receiveWalletSelBox.setOnAction(event -> {
            updateEstimatedReceiveAmountField();
        });

        trxWalletSelBox.setOnAction(event -> {
            Wallet selectedWallet = MainApplication.stateManager.getCurrentUser().getWallets().get(trxWalletSelBox.getSelectionModel().getSelectedIndex());
            transferWalletBalance.setText(selectedWallet.getUIFriendlyBalance() + " " + selectedWallet.getCryptoType());
        });

        payWalletSelBox.getSelectionModel().selectFirst();
        receiveWalletSelBox.getSelectionModel().select(1);
        trxWalletSelBox.getSelectionModel().selectFirst();

        convertBtn.setOnAction(event -> {
            //confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Convert");
            alert.setHeaderText("Are you sure you want to convert?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HttpClientHelper clientHelper = new HttpClientHelper();
                    if (clientHelper.convertCurrency(payWalletSelBox.getValue(), receiveWalletSelBox.getValue(), Double.parseDouble(payAmountField.getText()))) {
                        //clear fields
                        conversionAvlBalance.setText("");
                        transferWalletBalance.setText("");
                        payAmountField.clear();
                        receiveAmountValueField.clear();

                        //show success alert
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Currency conversion successful");
                        successAlert.showAndWait();

                        payWalletSelBox.getSelectionModel().selectFirst();
                        receiveWalletSelBox.getSelectionModel().select(1);
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Currency conversion failed");
                        errorAlert.showAndWait();
                    }
                }

                if (response == ButtonType.CANCEL) {
                    alert.close();
                }
            });

        });

        transferBtn.setOnAction(event -> {
            //confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Transfer");
            alert.setHeaderText("Are you sure you want to transfer?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HttpClientHelper clientHelper = new HttpClientHelper();
                    if (clientHelper.transferCurrency(trxWalletSelBox.getValue(), accountNameField.getText(), Double.parseDouble(trxAmountField.getText()))) {
                        //clear fields
                        conversionAvlBalance.setText("");
                        transferWalletBalance.setText("");
                        trxAmountField.clear();
                        accountNameField.clear();

                        //show success alert
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Currency transfer successful");
                        successAlert.showAndWait();

                        trxWalletSelBox.getSelectionModel().selectFirst();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Currency transfer failed");
                        errorAlert.showAndWait();
                    }
                }

                if (response == ButtonType.CANCEL) {
                    alert.close();
                }
            });

        });

    }

    void updateEstimatedReceiveAmountField() {

        if (payWalletSelBox.getSelectionModel().getSelectedItem() != null && receiveWalletSelBox.getSelectionModel().getSelectedItem() != null && !payAmountField.getText().isEmpty()) {
            Wallet payWallet = payWalletSelBox.getValue();
            Wallet receiveWallet = receiveWalletSelBox.getValue();

            double payAmount = Double.parseDouble(payAmountField.getText());
            double receiveAmount = payAmount * payWallet.getConversionRate(receiveWallet);
            receiveAmountValueField.setText(String.format("%.2f", receiveAmount) + " " + receiveWallet.getCryptoType());
        }
    }


}
