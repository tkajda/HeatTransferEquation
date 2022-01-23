import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

public class Plot extends Application {


    LineChart<Number,Number> lineChart;


    double[] result;
    XYChart.Series series = new XYChart.Series();


    public void init() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("y");

        this.lineChart = new LineChart<>(xAxis,yAxis);;
        List<String> args = getParameters().getRaw();
        IntegralSolver integralSolver = new IntegralSolver(Integer.parseInt(args.get(0)));
        this.result = integralSolver.getResult().toArray();
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);

    }



    @Override
    public void start(Stage primaryStage) throws Exception {


        for(int i = 0; i<result.length;i++) {

            series.getData().add(new XYChart.Data(i, result[i]));
        }

        Scene scene = new Scene(lineChart, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
