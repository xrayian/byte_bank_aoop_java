package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.utils.BinanceDataFetcher;
import com.kernelcrash.byte_bank.utils.CandleStickChart;
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
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DashboardMarketplaceController {

    @FXML
    private Label ltxt;

    @FXML
    private LineChart mainChart;

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

    @FXML
    private VBox primary_chart;

    private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");
    private final DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("MMM");

    @FXML
    private void initialize() {
        showLoader(true, "Loading data...");
        configureTableColumns();
        loadMarketplace();
        loadHistoricalData();
        setupUI();
        System.out.println("DashboardMarketplaceController initialized");
    }

    private void setupUI() {
        p_chart_label.setText("Marketplace Chart");
        tableTimeline.setCycleCount(Animation.INDEFINITE);
        tableTimeline.play();
    }

    private void configureTableColumns() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitCurrencyValueInUSD"));
        changeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal changePercentage, boolean empty) {
                super.updateItem(changePercentage, empty);
                if (empty || changePercentage == null) {
                    setText(0 + "%");
                } else {
                    setText(changePercentage.toPlainString() + "%");
                    getStyleClass().add("amount-cell");
                    if (changePercentage.compareTo(BigDecimal.ZERO) < 0) {
                        getStyleClass().add("negative");
                    }
                }
            }
        });
    }

    Timeline tableTimeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
        String timeStamp = LocalDateTime.now().format(fmt);
        initHashMap();
        updateTableLatestPricesChart();
        System.out.println("Updated table at " + timeStamp);
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

    private void loadHistoricalData() {
        Platform.runLater(() -> {
        try {
            List<BinanceDataFetcher.OHLCData> ohlcData = BinanceDataFetcher.fetchBinanceData("BTCUSDT", "1w", 40);
            // Populate new data
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (BinanceDataFetcher.OHLCData data : ohlcData) {
                //String time = LocalDateTime.ofEpochSecond(Long.parseLong(data.time) / 1000, 0, null).format(fmt2);
                //very slow this part
                series.getData().add(new XYChart.Data<>(data.date.toString(), data.close));
            }
            mainChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
    }

    private void showLoader(boolean isVisible, String message) {
        Platform.runLater(() -> {
            loaderPane.setVisible(isVisible);
            ltxt.setText(message != null ? message : "Loading data...");
        });
    }
}
