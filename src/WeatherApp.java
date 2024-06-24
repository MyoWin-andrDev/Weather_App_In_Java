import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
                if (cityLocationData != null) {
                    double latitude = (double) cityLocationData.get("latitude");
                    double longitude = (double) cityLocationData.get("longitude");
                    displayWeatherData(latitude, longitude);
                } else {
                    System.out.println("Could not retrieve location data for city: " + city);
                }

            } while (!city.equalsIgnoreCase("No"));
        }catch (Exception e){
            e.printStackTrace();
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
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(jsonResponse);

            //4. Retrieve Location Data
            JSONArray locationData = (JSONArray) resultJsonObj.get("results");
            return (JSONObject) locationData.get(0);
        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    public static HttpURLConnection fetchApiResponse(String urlString){

            //attempt to create connection
        try {
            URL url = null;
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //set request method to get
            conn.setRequestMethod("GET");

            return conn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //could not make connection
        //return null;

    }

    public static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            //Create a StringBuilder to store the resulting JSON data
            StringBuilder resultJson = new StringBuilder();

            //Create a Scanner to read from the InputStream of the HttpURLConnection
            Scanner scanner = new Scanner(apiConnection.getInputStream());

            //Loop through each line in the response and append it to the StringBuilder
            while (scanner.hasNext()) {
                //Read and append the current line to the StringBulder
                resultJson.append(scanner.nextLine());
            }
            //Close the Scanner to release resources associated with it
            scanner.close();
            //Return the JSON data as a String
            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void displayWeatherData(double latitude, double longitude){
        try{
            //1.Fetch the API response based on API Link
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m&hourly=temperature_2m,wind_speed_10m";
            HttpURLConnection apiConnection = fetchApiResponse(url);

            //Check for response status
            //200 - means that the connection was a success
            if(apiConnection.getResponseCode() !=200){
                System.out.println("Error : Could not load API");
                return;
            }

            //2. Read the response and convert store String type
            String jsonResponse = readApiResponse(apiConnection);

            //3. Parse the string into a JSON Object
            JSONParser parser = new JSONParser();
            JSONObject jsonObject =(JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");

            //4.Store the data into their corresponding data type
            String time = (String)currentWeatherJson.get("time");
            System.out.println("Current time " + time);

            double temperature = (double) currentWeatherJson.get("temperature_2m");
            System.out.println("Current Temperature(C): " + temperature);

            long relativeHumidity = (long)currentWeatherJson.get("relative_humidity_2m");
            System.out.println("Relative Humidity: " + relativeHumidity);

            double windSpeed = (double) currentWeatherJson.get("wind_speed_10m");
            System.out.println("Weather Description:" + windSpeed);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

