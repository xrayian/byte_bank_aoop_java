package com.kernelcrash.byte_bank.controllers.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.models.Wallet;
import com.kernelcrash.byte_bank.utils.CryptoDataFetcher;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.Icon;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @FXML
    private Label estBalVal;

    @FXML
    private Label walletCnt;

    private ComboBox<String> currencyDropdown = new ComboBox<>();

    //private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");
    private final DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

    @FXML
    private void initialize() {
        configureTableColumns();
        initializeCurrencyDropdown();
        loadHistoricalDataAndPopulateMainChart("BTC");
        setupUI();
        Platform.runLater(() -> {
            if (CurrencyDataStore.getLatestCurrencyPriceList().isEmpty()) {
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
        currencyDropdown.getStyleClass().add("ddButton");

        currencyDropdown.setOnAction(event -> {
            if (!mainChart.getData().isEmpty()) {
                mainChart.getData().clear();
            }

            String selectedCurrency = currencyDropdown.getSelectionModel().getSelectedItem();
            mainChart.getData().clear();

            Platform.runLater(() -> {
                loadHistoricalDataAndPopulateMainChart(selectedCurrency);
            });
        });

        primary_chart.getChildren().add(0, currencyDropdown);
    }

    private void setupUI() {
        estBalVal.setText("$" + CurrencyDataStore.getUserPortfolioValue());
        walletCnt.setText(MainApplication.stateManager.getCurrentUser().getWallets().size() + " Wallets");
        marketTable.setPlaceholder(new Label("Loading data..."));
        marketplaceTickTimelineHandler.setCycleCount(Animation.INDEFINITE);
        marketplaceTickTimelineHandler.play();
    }

    private void addWalletDetailsCardsToScene() {
        if (CurrencyDataStore.getLatestCurrencyPriceList().isEmpty() || CurrencyDataStore.getOHLCMap("BTC").isEmpty()) {
            return;
        }

        if (info_card_hbox.getChildren().size() == MainApplication.stateManager.getCurrentUser().getWallets().size()) {
            return;
        }

        info_card_hbox.getChildren().remove(1, info_card_hbox.getChildren().size());

        try {
            List<AnchorPane> cards = new ArrayList<>();
            for (Wallet wallet : MainApplication.stateManager.getCurrentUser().getWallets()) {
//                if (wallet.getBalance() == 0) continue;
                if (wallet.getCryptoType().equals("USD")) continue;
                if (CurrencyDataStore.getOHLCMap(wallet.getCryptoType()) == null) {
                    System.out.println("Fetching historical data for " + wallet.getCryptoType() + " within wallet adder");
                    CryptoDataFetcher.fetchHistoricalData(wallet.getCryptoType(), "USD", 24, 10);
                }

                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/cards/wallet-data-card.fxml"));
                AnchorPane card = loader.load();
                Label nameLabel = (Label) card.lookup("#curName");
                Label currencyUSDVal = (Label) card.lookup("#cur_val");
                Label currencyUSDOwned = (Label) card.lookup("#cur_own");
                LineChart chart = (LineChart) card.lookup("#cur_chart");


                if (nameLabel != null) nameLabel.setText(wallet.getCryptoType());

                if (currencyUSDVal != null)
                    //2 decimal places
                    currencyUSDVal.setText("$" + CurrencyDataStore.getLatestCurrencyPriceList().get(wallet.getCryptoType()).getUnitCurrencyValueInUSD().intValue());


                if (currencyUSDOwned != null)
                    currencyUSDOwned.setText("$" + CurrencyDataStore.getLatestCurrencyPriceList().get(wallet.getCryptoType()).getUnitCurrencyValueInUSD().multiply(BigDecimal.valueOf(wallet.getBalance())).intValue());

                if (chart != null) {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    if (CurrencyDataStore.getOHLCMap(wallet.getCryptoType()) == null) {
                        continue;
                    }
                    for (Map.Entry<Date, CryptoDataFetcher.OHLCData> entry : CurrencyDataStore.getOHLCMap(wallet.getCryptoType()).entrySet()) {
                        TemporalAccessor date = entry.getValue().date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        BigDecimal close = entry.getValue().close;
                        series.getData().add(new XYChart.Data<>(fmt2.format(date), close));
                    }
                    seriesMap.put(wallet.getCryptoType(), series);
                    chart.getData().add(seriesMap.get(wallet.getCryptoType()));
                    chart.getXAxis().setTickLabelsVisible(false);
                    chart.getYAxis().setTickLabelsVisible(false);
                }
                cards.add(card);
            }
            info_card_hbox.getChildren().addAll(cards);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureTableColumns() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitCurrencyValueInUSD"));
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("$" + item.intValue());
                }
            }
        });
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercentage"));
        changeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    FontIcon icon = new FontIcon();
                    if (item.compareTo(BigDecimal.ZERO) > 0) {
                        icon.setIconLiteral("fas-arrow-up");
                        icon.setIconSize(15);
                        icon.setIconColor(Paint.valueOf("green"));
                    } else {
                        icon.setIconLiteral("fas-arrow-down");
                        icon.setIconSize(15);
                        icon.setIconColor(Paint.valueOf("red"));
                    }
                    setGraphic(icon);
                    setText(item.abs().setScale(2, RoundingMode.HALF_UP) + "%");
                }
            }
        });

    }

    Timeline marketplaceTickTimelineHandler = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        updateTableLatestPricesChart();
        addWalletDetailsCardsToScene();
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

                //sort by time
                ohlcData = ohlcData.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));


                for (Map.Entry<Date, CryptoDataFetcher.OHLCData> entry : ohlcData.entrySet()) {

                    //TemporalAccessor date = entry.getValue().date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    BigDecimal close = entry.getValue().close;
                    series.getData().add(new XYChart.Data<>(entry.getValue().time, close));
                    series.getData().sort(Comparator.comparing(XYChart.Data::getXValue));
                }
                mainChart.getXAxis().setTickLabelsVisible(false);
//                mainChart.getYAxis().setTickLabelsVisible(false);
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
