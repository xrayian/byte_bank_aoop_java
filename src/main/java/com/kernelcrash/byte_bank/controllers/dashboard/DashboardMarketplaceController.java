package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.utils.CryptoDataFetcher;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    private ComboBox<String> currencyDropdown = new ComboBox<>();

    private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");
    private final DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    private void initialize() {
        showLoader(true, "Loading data...");
        configureTableColumns();
        loadMarketplace();
        populateDropdown();
        loadHistoricalData("BTC");
        setupUI();
        System.out.println("DashboardMarketplaceController initialized");
    }

    private void populateDropdown() {
        currencyDropdown.getItems().addAll(seriesMap.keySet());
        currencyDropdown.getSelectionModel().selectFirst();
        currencyDropdown.setId("currencyDropdown");
        currencyDropdown.setPlaceholder(new Label("Select a currency"));
        currencyDropdown.setOnAction(event -> {
            if (mainChart.getData().size() > 0) {
                mainChart.getData().clear();
            }
            String selectedCurrency = currencyDropdown.getSelectionModel().getSelectedItem();
            mainChart.getData().clear();
            loadHistoricalData(selectedCurrency);
        });
        primary_chart.getChildren().add(0, currencyDropdown);
    }

    private void setupUI() {
        tableTimeline.setCycleCount(Animation.INDEFINITE);
        tableTimeline.play();
    }

    private void configureTableColumns() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitCurrencyValueInUSD"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercentage"));
    }

    Timeline tableTimeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
        initHashMap();
        updateTableLatestPricesChart();
        if (currencyDropdown.getItems().size() != seriesMap.size()) {
            currencyDropdown.getItems().addAll(seriesMap.keySet());
        }
    }));

    private void initHashMap() {
        if (CurrencyDataStore.getLatestCurrencyPriceList().isEmpty() && seriesMap.isEmpty()) {
            //System.out.println("CurrencyDataStore is empty. Waiting for data...");
        } else if (seriesMap.isEmpty()) {
            CurrencyDataStore.getLatestCurrencyPriceList().forEach((key, value) -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(key);
                seriesMap.put(key, series);
            });
            mainChart.getData().addAll(seriesMap.values());
        }
    }

    private void updateTableLatestPricesChart() {
        ObservableList<CurrencyUSDValue> mostValuableCurrencies = FXCollections.observableArrayList(
                CurrencyDataStore.getLatestCurrencyPriceList().values().stream()
                        .sorted(Comparator.comparing(CurrencyUSDValue::getUnitCurrencyValueInUSD).reversed())
                        .limit(15)
                        .toList()
        );

        Platform.runLater(() -> {
            marketTable.setItems(mostValuableCurrencies);
            marketTable.refresh();
        });
        //System.out.println("Updated table with latest prices");
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

    private void loadHistoricalData(String symbol) {
        Platform.runLater(() -> {
            try {
                CryptoDataFetcher.fetchHistoricalData(symbol, "USD", 24, 10);
                System.out.println("Loading historical data...");
                HashMap<Date, CryptoDataFetcher.OHLCData> ohlcData = CurrencyDataStore.getOHLCMap();
                XYChart.Series<String, Number> series = new XYChart.Series<>();

                //sort by date
                ohlcData = ohlcData.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


                for (Map.Entry<Date, CryptoDataFetcher.OHLCData> entry : ohlcData.entrySet()) {
                    TemporalAccessor date = entry.getValue().date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    BigDecimal close = entry.getValue().close;
                    series.getData().add(new XYChart.Data<>(fmt2.format(date), close));
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
