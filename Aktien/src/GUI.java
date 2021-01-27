import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import javax.swing.text.html.CSS;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;


public class GUI extends Application{
    public Scanner Reader = new Scanner(System.in);
    WebRequest wr = new WebRequest();
    DB database = new DB();
    DS ds = new DS();
   @Override
   public void start(Stage s)
   {
       /*DB Klasse, WebRequest - Abfrage vom Symbol*/


       DB.CreateSTM();

       System.out.print("Welche Aktie wollen Sie aufrufen [TSLA][AAPL][AMZN]: ");
       String symbol = Reader.next().toUpperCase();
       /*Insert from APIHandler Data*/
       //database.InsertStatement(ds.getDate(), symbol, ds.getValue());
       /*DB OUTPUT*/
       System.out.print("Wollen Sie die Datenbank ausgeben?[y,n]: ");
       char choice = Reader.next().toLowerCase().charAt(0);

       Dia(s, "Date", "Value", symbol, wr.GetCloseValues(wr.Request(wr.StringBuilder(symbol))));
       if(choice == 'y') database.SelectStatement(symbol);
   }
   public void Dia(Stage s, String xLabel, String yLabel, String symbol, ArrayList<DS>data)
   {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(symbol);
        lineChart.setCreateSymbols(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Scene scene = new Scene(lineChart, 1080, 720);
        s.setScene(scene);
        for (DS d : data)
        {
            series.getData().add(new XYChart.Data<>(d.getDate(), d.getValue()));
        }
        lineChart.getData().add(series);
        series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        lineChart.setStyle("-fx-background-color: #0ef898;");
        lineChart.setCreateSymbols(false);
        series.setName("Close Werte");
        s.show();
   }
   public static void main(String args[]){
      launch(args);
   }
}