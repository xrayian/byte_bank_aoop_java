package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private void initialize() {
        setUserDetails();
        loadCoinList();
        loadPortfolio();
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
                last_login.setText("Last Login: "+stateManager.getCurrentUser().getUpdatedAt().format(formatter));
                acc_no.setText("Account No: "+stateManager.getCurrentUser().getUserId());
            }

            header_vbox.getChildren().set(0,headerCard);


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


    private void loadCoinList() {
        coin_vbox.getChildren().clear();
        addCryptoCard(coin_vbox, "Bitcoin (BTC)", "BTC 1.3043243");
        addCryptoCard(coin_vbox, "Ethereum (ETH)", "ETH 0.3043243");
        addCryptoCard(coin_vbox, "Ripple (XRP)", "XRP 0.3043243");
        addCryptoCard(coin_vbox, "Litecoin (LTC)", "LTC 0.3043243");
        addCryptoCard(coin_vbox, "Bitcoin Cash (BCH)", "BCH 0.3043243");
        addCryptoCard(coin_vbox, "Cardano (ADA)", "ADA 0.3043243");
        addCryptoCard(coin_vbox, "Polkadot (DOT)", "DOT 0.3043243");
//        addCryptoCard(coin_vbox, "Binance Coin (BNB)", "BNB 0.3043243");
//        addCryptoCard(coin_vbox, "Chainlink (LINK)", "LINK 0.3043243");
//        addCryptoCard(coin_vbox, "Stellar (XLM)", "XLM 0.3043243");
//        addCryptoCard(coin_vbox, "Tether (USDT)", "USDT 0.3043243");
//        addCryptoCard(coin_vbox, "Bitcoin SV (BSV)", "BSV 0.3043243");
//        addCryptoCard(coin_vbox, "Monero (XMR)", "XMR 0.3043243");
//        addCryptoCard(coin_vbox, "EOS (EOS)", "EOS 0.3043243");
//        addCryptoCard(coin_vbox, "TRON (TRX)", "TRX 0.3043243");
//        addCryptoCard(coin_vbox, "Tezos (XTZ)", "XTZ 0.3043243");
//        addCryptoCard(coin_vbox, "USD Coin (USDC)", "USDC 0.3043243");
//        addCryptoCard(coin_vbox, "VeChain (VET)", "VET 0.3043243");
//        addCryptoCard(coin_vbox, "Ethereum Classic (ETC)", "ETC 0.3043243");
//        addCryptoCard(coin_vbox, "Cosmos (ATOM)", "ATOM 0.3043243");
//        addCryptoCard(coin_vbox, "NEO (NEO)", "NEO 0.3043243");
//        addCryptoCard(coin_vbox, "Dai (DAI)", "DAI 0.3043243");
//        addCryptoCard(coin_vbox, "Huobi Token (HT)", "HT 0.3043243");
//        addCryptoCard(coin_vbox, "Zcash (ZEC)", "ZEC 0.3043243");
//        addCryptoCard(coin_vbox, "Maker (MKR)", "MKR 0.3043243");
//        addCryptoCard(coin_vbox, "Compound (COMP)", "COMP 0.3043243");
//        addCryptoCard(coin_vbox, "Dash (DASH)", "DASH 0.3043243");
//        addCryptoCard(coin_vbox, "OKB (OKB)", "OKB 0.3043243");
//        addCryptoCard(coin_vbox, "Aave (AAVE)", "AAVE 0.3043243");
    }


}
