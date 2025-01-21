package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.Wallet;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import com.kernelcrash.byte_bank.utils.HttpClientHelper;
import com.kernelcrash.byte_bank.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    VBox walletContainerVBOX;

    @FXML
    VBox portfolio_vbox;

    @FXML
    VBox header_vbox;

    @FXML
    Button addWalletBtn;


    @FXML
    private void initialize() {
        populateLoggedInUserList();
        loadUserWallets();
        addPortfolioCardsToScene();
        addWalletBtn.setOnAction(e -> {
            showWalletCreationModal();
        });
        System.out.println("DashboardHomeController initialized");
    }

    private void populateLoggedInUserList() {
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

    private void addPortfolioCardsToScene() {
        portfolio_vbox.getChildren().clear();
        stateManager.getCurrentUser().getWallets().forEach(
                wallet -> {
                    addPortfolioCard(portfolio_vbox, wallet.getCryptoType(), wallet.getBalance() + " " + wallet.getCryptoType());
                }
        );
//        addPortfolioCard(portfolio_vbox, "Total Assets Value", );
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

    private void addWalletCards(VBox container, Wallet wallet) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/crypto-card.fxml"));
            HBox cryptoCard = loader.load();
            Label nameLabel = (Label) cryptoCard.lookup(".crypto-name");
            Label valueLabel = (Label) cryptoCard.lookup(".crypto-value");
            if (nameLabel != null) nameLabel.setText(wallet.getWalletName() + " (" + wallet.getCryptoType() + ")");
            if (valueLabel != null)
                valueLabel.setText(String.format("%.2f", wallet.getBalance()) + " " + wallet.getCryptoType());
            container.getChildren().add(cryptoCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWalletCreationModal() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/wallet-creation-card.fxml"));
            AnchorPane walletCreationCard = loader.load();
            Scene walletCreationScene = new Scene(walletCreationCard, 400, 225);
            Stage walletCreationStage = new Stage();

            //pick the combo box inside the wallet creation card
            ComboBox<String> cryptoType = (ComboBox<String>) walletCreationCard.lookup("#cryptoType");
            cryptoType.getItems().add("Loading...");
            cryptoType.getItems().clear();
            cryptoType.getItems().addAll(CurrencyDataStore.getLatestCurrencyPriceList().keySet());
            cryptoType.getItems().sort(String::compareTo);
            cryptoType.getSelectionModel().selectFirst();

            TextField walletName = (TextField) walletCreationCard.lookup("#walletNameField");

            Button createWalletBtn = (Button) walletCreationCard.lookup("#createWalletBtn");

            if (createWalletBtn != null) {
                createWalletBtn.setOnAction(e -> {
                    HttpClientHelper httpClientHelper = new HttpClientHelper();
                    httpClientHelper.createWallet(walletName.getText(), cryptoType.getSelectionModel().getSelectedItem());
                    loadUserWallets();
                    walletCreationStage.close();
                });
            }

            Button cancelWalletBtn = (Button) walletCreationCard.lookup("#cancelBtn");

            cancelWalletBtn.setOnAction(e -> {
                walletCreationStage.close();
            });

            walletCreationStage.setTitle("Create Wallet");
            walletCreationStage.initModality(Modality.APPLICATION_MODAL);
            walletCreationStage.setScene(walletCreationScene);
            walletCreationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserWallets() {
        StateManager stateManager = StateManager.getInstance();
        walletContainerVBOX.getChildren().clear();
        stateManager.getCurrentUser().getWallets().forEach(
                wallet -> {
                    addWalletCards(walletContainerVBOX, wallet);
                }
        );

    }


}
