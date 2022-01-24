import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.List;

public class Plot extends Application {


    LineChart<Number,Number> lineChart;

    private static final double DOMAIN=2.0;

    double[] result;
    XYChart.Series series = new XYChart.Series();


    public void init() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("f(x)");

        this.lineChart = new LineChart<>(xAxis,yAxis);;
        List<String> args = getParameters().getRaw();
        Solver solver = new Solver(Integer.parseInt(args.get(0)));
        this.result = solver.getResult().toArray();
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);

    }



    @Override
    public void start(Stage primaryStage) throws Exception {


        for(int i = 0; i<result.length;i++) {

            double scaled = DOMAIN * i / (result.length - 1);
            series.getData().add(new XYChart.Data<>(scaled, result[i]));

        }

        Scene scene = new Scene(lineChart, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
