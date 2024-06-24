import org.json.simple.JSONObject;

import java.beans.Expression;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String city;
        try {
            do {
                //Retrieve User Input
                System.out.println("*===============================*");
                System.out.println("Enter City ( Say 'No' to Exit)");
                city = scanner.nextLine();

                if (city.equalsIgnoreCase("No")) break;

                //Get Location Data
                JSONObject cityLocationData = getLocationData(city);
                double latitude = (double) cityLocationData.get("Latitude");
                double longitude = (double) cityLocationData.get("Longitude");

                displayWeatherData(latitude, longitude);

            } while (!city.equalsIgnoreCase("No"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public static JSONObject getLocationData(String city){
    city = city.replaceAll(" ","+");

    String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city +"&count=1&language=en&format=json";

    try{
        //1. Fetch the API response based on API Link
        HttpURLConnection apiConnection = fetchApiResponse(urlString);

        //Check for response status
        //200 - means that the connection was a success
        if(apiConnection.getResponseCode() !=200){
            System.out.println("Error : Could not load API");
            return null;
        }

        //2.Read the response and convert store String type
        String jsonResponse = readApiResponse(apiConnection);

        //3.Parse the string into a JSON Object
    }catch (Exception e ){
        e.printStackTrace();
    }
}

public static HttpURLConnection fetchApiResponse(String urlString){
    try{
        //attempt to create connection
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //set request method to get
            conn.setRequestMethod("GET");

            return conn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //could not make connection
        return null;
}
