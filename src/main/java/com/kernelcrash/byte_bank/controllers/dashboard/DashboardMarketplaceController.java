package com.kernelcrash.byte_bank.controllers.dashboard;


import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.kernelcrash.byte_bank.utils.CurrencyDataStore;
import com.kernelcrash.byte_bank.utils.StompClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DashboardMarketplaceController {

    @FXML
    LineChart<String, Number> mainChart;

    @FXML
    Label p_chart_label; //primary chart label

    @FXML
    HBox info_card_hbox;

    private final HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm:ss");

    @FXML
    private void initialize() {
        loadMarketplace();
        setupUI();
        System.out.println("DashboardMarketplaceController initialized");
    }

    private void setupUI() {
        p_chart_label.setText("BTC/USD");
        System.out.println("Currency Store Size: " + CurrencyDataStore.getCurrencyList().size());
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initHashMap() {
        if (CurrencyDataStore.getCurrencyList().isEmpty() && seriesMap.isEmpty()) {
            System.out.println("CurrencyDataStore is empty. Waiting for data...");
        } else if (seriesMap.isEmpty()) {
            CurrencyDataStore.getCurrencyList().forEach((key, value) -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(key);
                seriesMap.put(key, series);
            });
            mainChart.getData().addAll(seriesMap.get("BTC"),seriesMap.get("ETH"),seriesMap.get("LTC"),seriesMap.get("XRP"));
        }
    }

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        String timeStamp = LocalDateTime.now().format(fmt);
        initHashMap();

        for (Map.Entry<String, XYChart.Series<String, Number>> entry : seriesMap.entrySet()) {
            String key = entry.getKey();
            XYChart.Series<String, Number> series = entry.getValue();
            CurrencyUSDValue currencyValue = CurrencyDataStore.getCurrencyList().get(key);
            if (currencyValue != null) {
                if (series.getData().size() > 10) {
                    series.getData().remove(0);
                }
                BigDecimal latestValue = currencyValue.getValue();
                series.getData().add(new XYChart.Data<>(timeStamp, latestValue));
            } else {
                System.err.println("No value found for key: " + key);
            }
        }
    }));

    public void loadMarketplace() {
        System.out.println("Loading Marketplace");
        try {
            StompClient.fetchCryptoDataFeed();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
