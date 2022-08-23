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
    public List<CityData> sameNameCitiesCount(String cityName)throws RemoteException, FileNotFoundException{
        List<CityData> cityDataList = jsonToCityData(JSON_FILE_NAME);
        List<CityData> list = new ArrayList<>();
        if(!cityDataList.isEmpty()){
            for (CityData cityData : cityDataList) {
                if (cityData.getName().equalsIgnoreCase(cityName)) {
                    list.add(cityData);
                }
            }
        }
        System.out.println();
        for (CityData cityData : list) {
            System.out.println(cityData.getId() + " " + cityData.getName() + " " + cityData.getCountry());
        }
        return list;
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
    public Map<String, Subscribe> addSubscribe(int cityId, String userName, String cityName, String subscribeTime) throws RemoteException {
        return subscribeCache.addSubscribe(cityId, userName, cityName, subscribeTime);
    }
}
