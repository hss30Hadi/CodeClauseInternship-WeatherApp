import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);

        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: Couldn't fetch location data");
            return null;
        }

        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn == null || conn.getResponseCode() != 200) {
                System.out.println("Error: Couldn't connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());

            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) parser.parse(resultJson.toString());

            JSONObject hourly = (JSONObject) resultJsonObject.get("hourly");
            if (hourly == null) {
                System.out.println("Error: No hourly data in API response");
                return null;
            }

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            if (temperatureData == null) {
                System.out.println("Error: No temperature data in API response");
                return null;
            }
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCodeData = (JSONArray) hourly.get("weather_code");
            if (weatherCodeData == null) {
                System.out.println("Error: No weather code data in API response");
                return null;
            }
            String weatherCondition = convertWeatherCode((long) weatherCodeData.get(index));

            JSONArray relativeHumidityData = (JSONArray) hourly.get("relative_humidity_2m");
            if (relativeHumidityData == null) {
                System.out.println("Error: No relative humidity data in API response");
                return null;
            }
            long humidity = (long) relativeHumidityData.get(index);

            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            if (windSpeedData == null) {
                System.out.println("Error: No wind speed data in API response");
                return null;
            }
            double windSpeed = (double) windSpeedData.get(index);

            JSONObject weatherData = new JSONObject();

            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windSpeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn == null || conn.getResponseCode() != 200) {
                System.out.println("Error: Couldn't connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());

            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) parser.parse(resultJson.toString());

            return (JSONArray) resultJsonObject.get("results");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' T 'HH':00'");
        return currentDateTime.format(formatter);
    }

    private static String convertWeatherCode(long weatherCode) {
        if (weatherCode == 0L) {
            return "Clear";
        } else if (weatherCode > 0L && weatherCode <= 3L) {
            return "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            return "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            return "Snow";
        }
        return "Unknown";
    }


}
