package com.kernelcrash.byte_bank.controllers.dashboard;

import javafx.scene.paint.Color;
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
    private TableColumn<TransactionItem, String> nameColumn;

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
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        populateTransactions();
    }

    private void populateTransactions() {
        // Add transactions to the table
        ObservableList<TransactionItem> transactions = FXCollections.observableArrayList(
                new TransactionItem("fas-arrow-right", "Bitcoin", "Deposit", "BTC", "+0.5", new Date()),
                new TransactionItem("fas-arrow-left", "Bitcoin", "Withdraw", "BTC", "-0.5", new Date()),
                new TransactionItem("fas-arrow-right", "Bitcoin", "Deposit", "BTC", "+0.5", new Date())
        );

        for (int i = 0; i < 60; i++) {
            transactions.add(new TransactionItem("fas-arrow-right", "Bitcoin", "Deposit", "BTC", "+0.5", new Date()));
        }

        transactionsTable.setItems(transactions);
    }


    public class TransactionItem {

        private final FontIcon icon;
        private final String name;
        private final String type;
        private final String currency;
        private final String amount;
        private final String dateTime;

        public TransactionItem(String iconLiteral, String name, String type, String currency, String amount, Date dateTime) {
//             Create a FontIcon using the literal
            this.icon = new FontIcon(iconLiteral);
            this.icon.setIconSize(20);              // Set icon size
            this.icon.setIconColor(Color.BLACK);    // Default color (can be customized)
            this.icon.getStyleClass().add("font-icon");         // Add a class name for CSS styling

            this.currency = currency;

            // Format the date
            String pattern = "E, dd MMM yyyy hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(dateTime);

            this.name = name;
            this.type = type;
            this.amount = amount;
            this.dateTime = date;


        }

        public FontIcon getIcon() {
            return icon;
        }

        public String getName() {
            return name;
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
