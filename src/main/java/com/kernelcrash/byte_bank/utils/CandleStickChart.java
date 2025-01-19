package com.kernelcrash.byte_bank.utils;

import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class CandleStickChart extends XYChart<String, Number> {
    public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis) {
        super(xAxis, yAxis);
        setAnimated(false);
    }

    @Override
    protected void layoutPlotChildren() {
        for (Data<String, Number> item : getData().get(0).getData()) {
            Candle candle = (Candle) item.getNode();
            candle.update(item.getXValue(), item.getYValue(), getYAxis().getDisplayPosition(item.getYValue()));
        }
    }

    @Override
    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {
        Candle candle = new Candle();
        item.setNode(candle);
        getPlotChildren().add(candle);
    }

    @Override
    protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
        getPlotChildren().remove(item.getNode());
    }

    @Override
    protected void dataItemChanged(Data<String, Number> item) {

    }

    @Override
    protected void seriesAdded(Series<String, Number> series, int seriesIndex) {

    }

    @Override
    protected void seriesRemoved(Series<String, Number> series) {

    }

    private class Candle extends StackPane {
        private Line highLowLine = new Line();
        private Rectangle body = new Rectangle();

        public Candle() {
            getChildren().addAll(highLowLine, body);
        }

        public void update(String x, Number y, double yAxisPosition) {
            highLowLine.setStartY(yAxisPosition - 10);
            highLowLine.setEndY(yAxisPosition + 10);
            body.setFill(Color.BLUE);
        }
    }
}
