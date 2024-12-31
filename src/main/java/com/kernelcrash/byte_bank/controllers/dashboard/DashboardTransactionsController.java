package com.kernelcrash.byte_bank.controllers.dashboard;

import javafx.scene.control.TableCell;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardTransactionsController {

    @FXML
    private TableView<TransactionItem> transactionsTable;

    @FXML
    private TableColumn<TransactionItem, FontIcon> iconColumn;

    @FXML
    private TableColumn<TransactionItem, String> accNameColumn;

    @FXML
    private TableColumn<TransactionItem, String> typeColumn;

    @FXML
    private TableColumn<TransactionItem, String> currencyColumn;

    @FXML
    private TableColumn<TransactionItem, String> amountColumn;

    @FXML
    private TableColumn<TransactionItem, String> dateTimeColumn;

    public void initialize() {
        // Set up columns
        iconColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(FontIcon icon, boolean empty) {
                super.updateItem(icon, empty);
                if (empty || icon == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(icon);
                    icon.getStyleClass().add("icon-cell");
                    icon.getStyleClass().add("font-icon");
                    icon.getStyleClass().add("start");
                }
            }
        });
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
        accNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(amount);
                    getStyleClass().add("amount-cell");
                    if (amount.startsWith("-")) {
                        getStyleClass().add("negative");
                    }
                }
            }
        });
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        dateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime);
                    getStyleClass().add("end");
                }
            }
        });
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        populateTransactions();
    }

    private void populateTransactions() {
        // Add transactions to the table
        ObservableList<TransactionItem> transactions = FXCollections.observableArrayList();

        for (int i = 0; i < 60; i++) {
            String accountName = 1012234436578853L * (i + 3 * 2) + "";
            if (i % 2 == 0)
                transactions.add(new TransactionItem("fas-arrow-left", accountName, "Withdrawal", "BTC", "-0.5", new Date()));
            else
                transactions.add(new TransactionItem("fas-arrow-right", accountName, "Deposit", "BTC", "+0.5", new Date()));
        }

        transactionsTable.setItems(transactions);
    }


    public class TransactionItem {

        @FXML
        private final FontIcon icon;

        @FXML
        private final String accountName;

        @FXML
        private final String type;

        @FXML
        private final String currency;

        @FXML
        private final String amount;

        @FXML
        private final String dateTime;

        public TransactionItem(String iconLiteral, String accountName, String type, String currency, String amount, Date dateTime) {

            String pattern = "E, dd MMM yyyy hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(dateTime);

            this.accountName = accountName;
            this.type = type;
            this.amount = amount;
            this.dateTime = date;
            this.icon = new FontIcon(iconLiteral);
            this.currency = currency;
        }

        public FontIcon getIcon() {
            return icon;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getType() {
            return type;
        }

        public String getCurrency() {
            return currency;
        }

        public String getAmount() {
            return amount;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

}
