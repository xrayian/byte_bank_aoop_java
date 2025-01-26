package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.Wallet;
import com.kernelcrash.byte_bank.utils.HttpClientHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Iterator;
import java.util.List;


public class DashboardSettingsController {

    @FXML
    private PasswordField oldPw;

    @FXML
    private PasswordField newPw1;

    @FXML
    private PasswordField newPw2;

    @FXML
    private ChoiceBox<Wallet> defaultWalletChoiceBox;

    @FXML
    Button updatePwallet;

    @FXML
    Button updatePasswordBtn;

    private Wallet oldPrimaryWallet;


    @FXML
    private void initialize() {
        setupUI();
        System.out.println("DashboardSettingsController initialized");
    }

    private void setupUI() {
        populateWalletList();
        updatePwallet.setOnAction(event -> {
            updateWallet();
        });

        updatePasswordBtn.setOnAction(event -> {
            updatePassword();
        });
    }

    private void updatePassword() {
        String oldPassword = oldPw.getText();
        String newPassword1 = newPw1.getText();
        String newPassword2 = newPw2.getText();

        if (oldPassword.isEmpty() || newPassword1.isEmpty() || newPassword2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("All fields are required");
            alert.showAndWait();
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("New passwords do not match");
            alert.showAndWait();
            return;
        }

        if (HttpClientHelper.updatePassword(oldPassword, newPassword1)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Password updated successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while updating the password");
            alert.showAndWait();
        }
    }

    private void updateWallet() {
        Wallet selectedWallet = defaultWalletChoiceBox.getValue();
        if (selectedWallet == null) {
            return;
        }

        if (oldPrimaryWallet == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Primary Wallet");
        alert.setHeaderText("Are you sure you want to update your primary wallet?");
        alert.setContentText("This action cannot be undone");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.CANCEL) {
                return;
            }

            if (HttpClientHelper.updatePrimaryWallet(selectedWallet, oldPrimaryWallet)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Primary wallet updated successfully");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("An error occurred while updating the primary wallet");
                errorAlert.showAndWait();
            }
        });

    }


    private void populateWalletList() {
        List<Wallet> wallets = MainApplication.stateManager.getCurrentUser().getWallets();
        if (wallets == null || wallets.isEmpty()) {
            return;
        }

        if (wallets.size() == 1) {
            defaultWalletChoiceBox.getItems().add(wallets.get(0));
            defaultWalletChoiceBox.getSelectionModel().select(0);
            defaultWalletChoiceBox.setDisable(true);
            return;
        }

        // Add all wallets to the ChoiceBox
        defaultWalletChoiceBox.getItems().addAll(wallets);

        // Use an iterator to avoid ConcurrentModificationException
        Iterator<Wallet> iterator = defaultWalletChoiceBox.getItems().iterator();
        while (iterator.hasNext()) {
            Wallet wallet = iterator.next();
            if (wallet.isPrimary()) {
                oldPrimaryWallet = wallet;
                iterator.remove(); // Safely remove the wallet
            }
        }

        // Disable the ChoiceBox if all items are removed
        if (defaultWalletChoiceBox.getItems().isEmpty()) {
            defaultWalletChoiceBox.setDisable(true);
        }
    }



}
