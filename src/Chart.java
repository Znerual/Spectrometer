import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Map;

public class Chart extends Application {
    float[] values;
    String[] frequ;
    public static Map<Integer, Float> data;
    public static int startFreq;
    public static int endFreq;
    public static int step;

    @Override
    public void start(Stage stage) {
        values = new float[data.size()];
        frequ = new String[data.size()];
        int count = 0;
        for (int i = startFreq;i < endFreq; i += step) {
            frequ[count] = String.valueOf(i);
            values[count] = data.get(i) == null ? 0 : data.get(i);
            count++;
        }
        stage.setTitle("Fourier Analysis");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Fourier Analysis");
        xAxis.setLabel("Frequency");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Data");
        for (int i = 0; i < values.length; i++) {
            series1.getData().add(new XYChart.Data(frequ[i], values[i]));
        }




        Scene scene = new Scene(bc, 800, 600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}