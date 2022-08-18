import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OpenWeatherMapJsonParser implements WeatherParser{
    private final static String API_CALL_TEMPLATE = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final static String API_CALL_TEMPLATE_ID = "https://api.openweathermap.org/data/2.5/weather?id=";
    private final static String API_KEY_TEMPLATE = "&units=metric&APPID=de834929791b3dbe8e75a9cba9eaaf2a";
    private final static String API_KEY_TEMPLATE_ID = "&appid=de834929791b3dbe8e75a9cba9eaaf2a";
    private final static String API_LAT_TEMPLATE = "http://api.openweathermap.org/geo/1.0/reverse?lat=";
    private final static String API_LON_TEMPLATE = "&lon=";
    private final static String API_KEY_TEMPLATE_COORDINATE = "&limit=3&appid=de834929791b3dbe8e75a9cba9eaaf2a";
    private final static String USER_AGENT = "Chrome/104.0.0.0";
    private final static DateTimeFormatter INPUT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter OUTPUT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MMM dd", Locale.US);
    
    @Override
    public String getReadyForecast(String city) {
        String result;
        try {
            String jsonRawData = downloadJsonRawData(city);
            List<String> linesOfForecast = convertRawDataToList(jsonRawData);
            result = String.format("%s:%s%s", city, System.lineSeparator(), parseForecastDataFromList(linesOfForecast));
        } catch (IllegalArgumentException e) {
            return String.format("Can't find \"%s\" city. Try another one, for example: \"Dnipro\" or \"Manchester\"", city);
        } catch (Exception e) {
            e.printStackTrace();
            return "The service is not available, please try later";
        }
        return result;
    }

    @Override
    public String getReadyForecastWithThreeHourStep(String city) {
        String result;
        try {
            String jsonRawData = downloadJsonRawData(city);
            List<String> linesOfForecast = convertRawDataToListWithThreeHourStep(jsonRawData);
            result = String.format("%s:%s%s", city, System.lineSeparator(), parseForecastDataFromList(linesOfForecast));
        } catch (IllegalArgumentException e) {
            return String.format("Can't find \"%s\" city. Try another one, for example: \"Dnipro\" or \"Manchester\"", city);
        } catch (Exception e) {
            e.printStackTrace();
            return "The service is not available, please try later";
        }
        return result;
    }

    @Override
    public String getReadyForecastCoordinate(double lat, double lot) {
        String result;
        try {
            String jsonRawData = downloadJsonRawDataCoordinate(lat, lot);
            List<String> linesOfForecast = convertRawDataToListCoordinate(jsonRawData);
            String city = parseForecastDataFromListCoordinate(linesOfForecast);
            result = getReadyForecast(city);
        } catch (IllegalArgumentException e) {
            return String.format("Can't find \"%s%s\" coordinate. Try another one, for example: \"48.0000\" or \"51.00\"");
        } catch (Exception e) {
            e.printStackTrace();
            return "The service is not available, please try lateR";
        }
        return result;
    }

    @Override
    public String getReadyForecastById(int cityId) {
        String result;
        try{
            String jsonRawData = downloadJsonRawDataById(cityId);
            System.out.println(jsonRawData);
            System.out.println();
//            List<String> linesOfForecast = convertRawDataToListCoordinate(jsonRawData);
//            System.out.println(linesOfForecast);
            String s = parseForecastDataFromListById(jsonRawData);
            result = String.format("%s:%s%s", cityId, System.lineSeparator(), /*parseForecastDataFromListById(linesOfForecast)*/s);
        }catch (IllegalArgumentException e) {
            return String.format("Can't find \"%s\" city. Try another one, for example: \"Dnipro\" or \"Manchester\"", cityId);
        } catch (Exception e) {
            e.printStackTrace();
            return "The service is not available, please try later";
        }
        return result;
    }

    private static String downloadJsonRawDataCoordinate(double lat, double lot) throws Exception{
        String urlString = API_LAT_TEMPLATE + lat + API_LON_TEMPLATE + lot + API_KEY_TEMPLATE_COORDINATE;
        URL urlObject = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static String downloadJsonRawDataById(int cityId) throws Exception{
        String urlString = API_CALL_TEMPLATE_ID + cityId + API_KEY_TEMPLATE_ID;
        URL urlObject = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static String downloadJsonRawData(String city) throws Exception {
        String urlString = API_CALL_TEMPLATE + city + API_KEY_TEMPLATE;
        URL urlObject = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static List<String> convertRawDataToListWithThreeHourStep(String data) throws Exception {
        List<String> weatherList = new ArrayList<>();
        int i = 0;

        JsonNode arrNode = new ObjectMapper().readTree(data).get("list");
        if (arrNode != null && arrNode.isArray()) {
            for (final JsonNode objNode : arrNode) {
                if(i < 3){
                    weatherList.add(objNode.toString());
                    i++;
                }
                else{
                    break;
                }
            }
        }
        return weatherList;
    }


    private static List<String> convertRawDataToList(String data) throws Exception {
        List<String> weatherList = new ArrayList<>();

        JsonNode arrNode = new ObjectMapper().readTree(data).get("list");
        if (arrNode != null && arrNode.isArray()) {
            for (final JsonNode objNode : arrNode) {
                weatherList.add(objNode.toString());
                break;
            }
        }
        return weatherList;
    }

    private static List<String> convertRawDataToListCoordinate(String data) throws Exception {
        List<String> weatherList = new ArrayList<>();

        JsonNode arrNode = new ObjectMapper().readTree(data);
        if (arrNode != null && arrNode.isArray()) {
            for (final JsonNode objNode : arrNode) {
                weatherList.add(objNode.toString());
            }
        }
        return weatherList;
    }


    private static String parseForecastDataFromListCoordinate(List<String> weatherList) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        for (String line : weatherList) {
            {

                JsonNode weatherArrNode;
                String s = "";
                try {
                    weatherArrNode = objectMapper.readTree(line).get("name");
                    s = weatherArrNode.toString();
                    s = s.replace("\"", "");
                    return s;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static String parseForecastDataFromListById(String data) throws Exception{
        final StringBuffer sb = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode mainNode;
        JsonNode weatherArrNode;
        JsonNode cloudsNode;

        try{
            mainNode = objectMapper.readTree(data).get("main");
            weatherArrNode = objectMapper.readTree(data).get("weather");

            for (final JsonNode objNode : weatherArrNode) {
                cloudsNode = objectMapper.readTree(data).get("clouds");
                sb.append(formatForecastDataByID(objNode.get("main").toString(), mainNode.get("temp").asDouble(), cloudsNode.toString()));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

//    private static String parseForecastDataFromListById(List<String> weatherList) throws Exception{
//        final StringBuffer sb = new StringBuffer();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        for(String line : weatherList){
//            JsonNode mainNode;
//            JsonNode weatherArrNode;
//            JsonNode cloudsNode;
//
//            try{
//                mainNode = objectMapper.readTree(line).get("main");
//                weatherArrNode = objectMapper.readTree(line).get("weather");
//
//                for (final JsonNode objNode : weatherArrNode) {
//                    cloudsNode = objectMapper.readTree(line).get("clouds");
//                    sb.append(formatForecastDataByID(objNode.get("main").toString(), mainNode.get("temp").asDouble(), cloudsNode.toString()));
//                }
//
//            }catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return sb.toString();
//    }

    private static String parseForecastDataFromList(List<String> weatherList) throws Exception {
        final StringBuffer sb = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String line : weatherList) {
            {
                String dateTime;
                JsonNode mainNode;
                JsonNode weatherArrNode;
                JsonNode cloudsNode;

                try {
                    mainNode = objectMapper.readTree(line).get("main");
                    weatherArrNode = objectMapper.readTree(line).get("weather");

                    for (final JsonNode objNode : weatherArrNode) {
                        dateTime = objectMapper.readTree(line).get("dt_txt").toString();
                        cloudsNode = objectMapper.readTree(line).get("clouds");
                        sb.append(formatForecastData(dateTime, objNode.get("main").toString(), mainNode.get("temp").asDouble(), cloudsNode.toString()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static String formatForecastDataByID(String description, double temperature, String clouds) throws Exception {
//        LocalDateTime forecastDateTime = LocalDateTime.parse(date.replaceAll("\"", ""), INPUT_DATE_TIME_FORMAT);
//        String formattedDateTime = forecastDateTime.format(OUTPUT_DATE_TIME_FORMAT);

        String formattedTemperature;
        long roundedTemperature = Math.round(temperature);
        if (roundedTemperature > 0) {
            formattedTemperature = "+" + String.valueOf(Math.round(temperature));
        } else {
            formattedTemperature = String.valueOf(Math.round(temperature));
        }

        String formattedDescription = description.replaceAll("\"", "");
        String formattedClouds = clouds.replaceAll("[^0-9]", "");

//        String weatherIconCode = WeatherUtils.weatherIconsCodes.get(formattedDescription);

        return String.format("  %s %s %s clouds: %s", formattedTemperature, formattedDescription, System.lineSeparator(), formattedClouds);
    }

    private static String formatForecastData(String date, String description, double temperature, String clouds) throws Exception {
        LocalDateTime forecastDateTime = LocalDateTime.parse(date.replaceAll("\"", ""), INPUT_DATE_TIME_FORMAT);
        String formattedDateTime = forecastDateTime.format(OUTPUT_DATE_TIME_FORMAT);

        String formattedTemperature;
        long roundedTemperature = Math.round(temperature);
        if (roundedTemperature > 0) {
            formattedTemperature = "+" + String.valueOf(Math.round(temperature));
        } else {
            formattedTemperature = String.valueOf(Math.round(temperature));
        }

        String formattedDescription = description.replaceAll("\"", "");
        String formattedClouds = clouds.replaceAll("[^0-9]", "");

//        String weatherIconCode = WeatherUtils.weatherIconsCodes.get(formattedDescription);

        return String.format("%s   %s %s %s clouds: %s \n", formattedDateTime, formattedTemperature, formattedDescription, System.lineSeparator(), formattedClouds);
    }
}
