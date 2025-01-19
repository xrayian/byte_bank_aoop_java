package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import com.kernelcrash.byte_bank.utils.StompClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DashboardMarketplaceController {

    @FXML
    private Label ltxt;

    @FXML
    private LineChart<String, Number> mainChart;

    @FXML
    private Label p_chart_label;

    @FXML
    private HBox info_card_hbox;

    @FXML
    private TableView<CurrencyUSDValue> marketTable;

    @FXML
    private TableColumn<CurrencyUSDValue, String> symbolColumn;

    @FXML
    private TableColumn<CurrencyUSDValue, String> nameColumn;

    @FXML
    private TableColumn<CurrencyUSDValue, BigDecimal> priceColumn;

    @FXML
    private TableColumn<CurrencyUSDValue, BigDecimal> changeColumn;

    @FXML
    private StackPane loaderPane;

    private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");

    @FXML
    private void initialize() {
        showLoader(true, "Loading data...");
        configureTableColumns();
        loadMarketplace();
        setupUI();
        System.out.println("DashboardMarketplaceController initialized");
    }

    private void setupUI() {
        p_chart_label.setText("Marketplace Chart");
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void configureTableColumns() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitCurrencyValueInUSD"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercentage"));
    }

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        String timeStamp = LocalDateTime.now().format(fmt);
        initHashMap();
        updateTableLatestPricesChart();

        for (Map.Entry<String, XYChart.Series<String, Number>> entry : seriesMap.entrySet()) {
            String key = entry.getKey();
            XYChart.Series<String, Number> series = entry.getValue();
            CurrencyUSDValue currencyValue = CurrencyDataStore.getCurrencyList().get(key);
            if (mainChart.getData().contains(series)) {
                if (currencyValue != null) {
                    if (series.getData().size() > 10) {
                        series.getData().remove(0);
                    }
                    BigDecimal latestValue = currencyValue.getUnitCurrencyValueInUSD();
                    series.getData().add(new XYChart.Data<>(timeStamp, latestValue));
                    System.out.println("Added data to series: " + key + " | " + latestValue);
                } else {
                    System.err.println("No value found for key: " + key);
                }
            }
        }
    }));

    private void initHashMap() {
        if (CurrencyDataStore.getCurrencyList().isEmpty() && seriesMap.isEmpty()) {
            System.out.println("CurrencyDataStore is empty. Waiting for data...");
        } else if (seriesMap.isEmpty()) {
            CurrencyDataStore.getCurrencyList().forEach((key, value) -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(key);
                seriesMap.put(key, series);
            });
            mainChart.getData().addAll(seriesMap.get("BTC"), seriesMap.get("ETH"), seriesMap.get("LTC"));
        }
    }

    private void updateTableLatestPricesChart() {
        ObservableList<CurrencyUSDValue> mostValuableCurrencies = FXCollections.observableArrayList(
                CurrencyDataStore.getCurrencyList().values().stream()
                        .sorted(Comparator.comparing(CurrencyUSDValue::getUnitCurrencyValueInUSD).reversed())
                        .limit(15)
                        .toList()
        );

        Platform.runLater(() -> {
            marketTable.setItems(mostValuableCurrencies);
            marketTable.refresh();
        });

        System.out.println("Updated table with latest prices");
    }

    public void loadMarketplace() {
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    StompClient.fetchCryptoDataFeed();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                showLoader(false, null);
            }

            @Override
            protected void failed() {
                super.failed();
                showLoader(false, "Failed to load data.");
            }
        };

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void showLoader(boolean isVisible, String message) {
        Platform.runLater(() -> {
            loaderPane.setVisible(isVisible);
            ltxt.setText(message != null ? message : "Loading data...");
        });
    }
}
