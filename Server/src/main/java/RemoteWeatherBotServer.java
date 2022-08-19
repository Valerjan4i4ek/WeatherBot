import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.*;

public class RemoteWeatherBotServer implements WeatherBot{
    private final static String JSON_FILE_NAME = "Server/citylist.json";
    AuthorizationCache authorizationCache = new AuthorizationCache();
    SubscribeCache subscribeCache = new SubscribeCache();
    OpenWeatherMapJsonParser openWeatherMapJsonParser = new OpenWeatherMapJsonParser();

    private static List<CityData> jsonToCityData(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), CityData[].class));
    }

    @Override
    public String checkAuthorization(String login, String password) throws RemoteException {
        return authorizationCache.checkAuthorization(login, password);
    }

    @Override
    public Map<Integer, Map<String, String>> sameNameCitiesCount(String cityName) throws RemoteException, FileNotFoundException {
        List<CityData> cityDataList = jsonToCityData(JSON_FILE_NAME);
        Map<Integer, Map<String, String>> returnMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        if(!cityDataList.isEmpty()){
            for (int i = 0; i < cityDataList.size(); i++) {
                if(cityDataList.get(i).getName().equalsIgnoreCase(cityName)){
                    innerMap.put(cityDataList.get(i).getName(), cityDataList.get(i).getCountry());
                    returnMap.put(cityDataList.get(i).getId(), innerMap);
                }
            }
        }
        for(Map.Entry<Integer, Map<String, String>> entry : returnMap.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return returnMap;
    }

    @Override
    public String getReadyForecast(String city) throws RemoteException {
        return openWeatherMapJsonParser.getReadyForecast(city);
    }

    @Override
    public String getReadyForecastCoordinate(double lat, double lot) throws RemoteException {
        return openWeatherMapJsonParser.getReadyForecastCoordinate(lat, lot);
    }

    @Override
    public String getReadyForecastById(int cityId) throws RemoteException {
        return openWeatherMapJsonParser.getReadyForecastById(cityId);
    }

    @Override
    public String getReadyForecastWithThreeHourStep(String userName) throws RemoteException {
        return subscribeCache.getReadyForecastWithThreeHourStep(userName);
    }

    @Override
    public String getSubscribeTimeByUserName(String userName) throws RemoteException {
        return subscribeCache.getSubscribeTimeByUserName(userName);
    }

    @Override
    public String addSubscribe(String userName, String cityName, String subscribeTime) throws RemoteException {
        return subscribeCache.addSubscribe(userName, cityName, subscribeTime);
    }
}
