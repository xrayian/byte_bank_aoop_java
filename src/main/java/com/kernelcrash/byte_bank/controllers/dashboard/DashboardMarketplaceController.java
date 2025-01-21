package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.utils.CryptoDataFetcher;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    //private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");
    private final DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    private void initialize() {
        configureTableColumns();
        initializeCurrencyDropdown();
        loadHistoricalDataAndPopulateMainChart("BTC");
        setupUI();
        Platform.runLater(() -> {
            if(CurrencyDataStore.getLatestCurrencyPriceList().isEmpty()) {
                showLoader(true, "Loading data...");
            } else {
                showLoader(false, null);
            }
        });
        System.out.println("DashboardMarketplaceController Initialized...");
    }

    //Populates the main chart with the available currencies
    private void initializeCurrencyDropdown() {

        currencyDropdown.setPlaceholder(new Label("Select a currency"));
        currencyDropdown.getItems().addAll(CurrencyDataStore.getLatestCurrencyPriceList().keySet());
        currencyDropdown.getItems().sort(Comparator.naturalOrder());
        currencyDropdown.getSelectionModel().select("BTC");

        currencyDropdown.setOnAction(event -> {
            // todo change the chart title and set chart data from fetched historical data
            if (!mainChart.getData().isEmpty()) {
                mainChart.getData().clear();
            }

            String selectedCurrency = currencyDropdown.getSelectionModel().getSelectedItem();
            mainChart.getData().clear();

            Platform.runLater(() -> {
//                p_chart_label.setText("Historical data for " + selectedCurrency);
                loadHistoricalDataAndPopulateMainChart(selectedCurrency);
            });
        });

        primary_chart.getChildren().add(0, currencyDropdown);
    }

    private void setupUI() {
        marketTable.setPlaceholder(new Label("Loading data..."));
        tableTimeline.setCycleCount(Animation.INDEFINITE);
        tableTimeline.play();
    }

    private void configureTableColumns() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitCurrencyValueInUSD"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercentage"));
    }

    Timeline tableTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        updateTableLatestPricesChart();
    }));

    private void updateTableLatestPricesChart() {
        ObservableList<CurrencyUSDValue> mostValuableCurrencies =
                FXCollections.observableArrayList(
                        CurrencyDataStore.getLatestCurrencyPriceList().values().stream()
                                .sorted(Comparator.comparing(CurrencyUSDValue::getUnitCurrencyValueInUSD).reversed())
                                .limit(15)
                                .toList());

        Platform.runLater(() -> {
            marketTable.setItems(mostValuableCurrencies);
            marketTable.refresh();
            showLoader(false, null);
        });
        //System.out.println("Updated table with latest prices");
    }


    private void loadHistoricalDataAndPopulateMainChart(String symbol) {
        Platform.runLater(() -> {
            try {
                //fetch historical data and save it to the CurrencyDataStore
                CryptoDataFetcher.fetchHistoricalData(symbol, "USD", 24, 10);
                System.out.println("Loading historical data...");
                Map<Date, CryptoDataFetcher.OHLCData> ohlcData = CurrencyDataStore.getOHLCMap(symbol);
                XYChart.Series<String, Number> series = new XYChart.Series<>();

                //sort by date
                CurrencyDataStore.getOHLCMap(symbol).entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


                for (Map.Entry<Date, CryptoDataFetcher.OHLCData> entry : ohlcData.entrySet()) {
                    TemporalAccessor date = entry.getValue().date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    BigDecimal close = entry.getValue().close;
                    series.getData().add(new XYChart.Data<>(fmt2.format(date), close));
                }

                mainChart.getData().add(series); //this probably is the line responsible for the change

            } catch (Exception e) {
                System.err.println(e);
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
