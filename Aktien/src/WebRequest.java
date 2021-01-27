import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.*;

public class WebRequest
{

    private final String key="&apikey=1AD6CE6LV8OFT02F";
    private String requestString = "https://www.alphavantage.co/query?";
    private String function = "function=TIME_SERIES_DAILY&";
    private String prefsymbol = "&symbol=";

    public JSONObject Request(String urlString)
    {


        HttpURLConnection con;
        try
        {
            URL url = new URL(urlString);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try(BufferedReader br = new BufferedReader((new InputStreamReader(con.getInputStream(),StandardCharsets.UTF_8))))
            {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while((responseLine = br.readLine()) != null)
                {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                return new JSONObject(response.toString());
            }
        }catch (Exception e)
         {
            System.out.println("Der requestString kann entweder nicht gefunden werden, oder nicht geöffnet werden");
            e.printStackTrace();
         }
        return null;
    }
    public String StringBuilder(String symbol)
    {
        String s = requestString+function+prefsymbol+symbol+key;

        return s;
    }
    public ArrayList<DS>GetCloseValues(JSONObject json)
    {
        ArrayList<DS> arrayList = new ArrayList<>();

        try
        {
            JSONObject info = json.getJSONObject("Meta Data");
            String symbol =(String) info.get("2. Symbol");
            DB.CreateTable(symbol);
            JSONObject result = json.getJSONObject("Time Series (Daily)");
            for(int i = 0; i < result.length(); i++)
            {
                String date = result.names().get(i).toString();
                JSONObject val = (JSONObject)result.get(date);
                Double wert = Double.parseDouble(val.get("4. close").toString());
                arrayList.add(new DS(date, wert));
                DB.InsertStatement(symbol,date,wert);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return arrayList;
    }
}