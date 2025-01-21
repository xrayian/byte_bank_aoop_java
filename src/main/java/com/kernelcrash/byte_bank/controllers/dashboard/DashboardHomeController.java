package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import com.kernelcrash.byte_bank.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DashboardHomeController {

    StateManager stateManager = StateManager.getInstance();

    @FXML
    VBox coin_vbox;

    @FXML
    VBox portfolio_vbox;

    @FXML
    VBox header_vbox;

    @FXML
    Button addWalletBtn;


    @FXML
    private void initialize() {
        setUserDetails();
        loadCoinList();
        loadPortfolio();
        addWalletBtn.setOnAction(e -> {
            //showCustomAlert("Coming Soon", "This feature is coming soon. Stay tuned!");
            showWalletCreationCard();
        });
        System.out.println("DashboardHomeController initialized");
    }

    private void setUserDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/dashboard-home-header.fxml"));
            HBox headerCard = loader.load();

            Label username = (Label) headerCard.lookup(".user-name");
            Label email = (Label) headerCard.lookup(".user-email");
            Label last_login = (Label) headerCard.lookup(".last-login");
            Label acc_no = (Label) headerCard.lookup(".account-info");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            if (stateManager.getCurrentUser() != null) {

                username.setText(stateManager.getCurrentUser().getUsername());
                email.setText(stateManager.getCurrentUser().getEmail());
                last_login.setText("Last Login: " + stateManager.getCurrentUser().getUpdatedAt().format(formatter));
                acc_no.setText("Account No: " + stateManager.getCurrentUser().getUserId());
            }

            header_vbox.getChildren().set(0, headerCard);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPortfolio() {
        portfolio_vbox.getChildren().clear();
        addPortfolioCard(portfolio_vbox, "Total Assets Value", "USD 345,650.34");
        addPortfolioCard(portfolio_vbox, "Transactions (Last 60 Days)", "USD 12,230.00");
        addPortfolioCard(portfolio_vbox, "Portfolio Profits", "USD 21,235.93");
    }

    private void addPortfolioCard(VBox container, String name, String value) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/portfolio-card.fxml"));
            HBox portfolioCard = loader.load();

            // Set the portfolio name and value dynamically
            Label nameLabel = (Label) portfolioCard.lookup(".portfolio-title");
            Label valueLabel = (Label) portfolioCard.lookup(".portfolio-value");

            if (nameLabel != null) nameLabel.setText(name);
            if (valueLabel != null) valueLabel.setText(value);

            container.getChildren().add(portfolioCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCryptoCard(VBox container, String name, String value) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/crypto-card.fxml"));
            HBox cryptoCard = loader.load();

            // Set the crypto name and value dynamically
            Label nameLabel = (Label) cryptoCard.lookup(".crypto-name");
            Label valueLabel = (Label) cryptoCard.lookup(".crypto-value");

            if (nameLabel != null) nameLabel.setText(name);
            if (valueLabel != null) valueLabel.setText(value);

            container.getChildren().add(cryptoCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWalletCreationCard() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/wallet-creation-card.fxml"));
            AnchorPane walletCreationCard = loader.load();
            Scene walletCreationScene = new Scene(walletCreationCard, 400,225);
            Stage walletCreationStage = new Stage();

            //pick the combo box inside the wallet creation card
            ComboBox<String> cryptoType = (ComboBox<String>) walletCreationCard.lookup("#cryptoType");
            cryptoType.getItems().addAll(CurrencyDataStore.getLatestCurrencyPriceList().keySet());
            cryptoType.getSelectionModel().selectFirst();

            walletCreationStage.setTitle("Create Wallet");
            walletCreationStage.initModality(Modality.APPLICATION_MODAL);
            walletCreationStage.setScene(walletCreationScene);
            walletCreationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void showCustomAlert(String title, String message) {
//        // Create a new Stage for the custom alert
//        Stage alertStage = new Stage();
//        alertStage.initModality(Modality.APPLICATION_MODAL);
//        alertStage.setTitle(title);
//
//        // Create custom labels and Input fields
//        //wallet creation request
//
//
//        //
//        Button closeButton = new Button("Close");
//        closeButton.setOnAction(e -> alertStage.close());
//
//
//        VBox layout = new VBox(10, titleLabel, messageLabel, closeButton);
//        layout.setStyle("-fx-padding: 20;");
//        Scene alertScene = new Scene(layout, 300, 150);
//
//        alertStage.setScene(alertScene);
//        alertStage.showAndWait();
//    }


    private void loadCoinList() {
        StateManager stateManager = StateManager.getInstance();
        coin_vbox.getChildren().clear();
        stateManager.getCurrentUser().getWallets().forEach(
                wallet -> {
                    addCryptoCard(coin_vbox, wallet.getCryptoType(), wallet.getBalance() + " " + wallet.getCryptoType());
                }
        );

    }


}
