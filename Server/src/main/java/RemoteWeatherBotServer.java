import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RemoteWeatherBotServer implements WeatherBot{
    private final static String JSON_FILE_NAME = "Server/citylist.json";
    MySQLClass sql = new MySQLClass();
    OpenWeatherMapJsonParser openWeatherMapJsonParser = new OpenWeatherMapJsonParser();
    List<Integer> listUsers;
    List<Integer> listSubscribe;
    int countAuthorization;
    int countSubscribe;

    private static List<CityData> jsonToCityData(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), CityData[].class));
    }

    @Override
    public String checkAuthorization(String login, String password) throws RemoteException {
        String s = sql.checkUser(login, password);
        if(s != null && !s.isEmpty()){
            if(s.equals("NEW REGISTRATION")){
                System.out.println(s);
                incrementAuthorization();
                sql.addAuthorization(new User(countAuthorization, login, password));
                return s;
            }
            else{

                System.out.println(s);
                return s;
            }
        }
        else {
            incrementAuthorization();
            sql.addAuthorization(new User(countAuthorization, login, password));
            System.out.println("new registration");
            return "new registration";
        }
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
    public String getReadyForecast(String city) throws RemoteException, FileNotFoundException {
        return openWeatherMapJsonParser.getReadyForecast(city);
    }

    @Override
    public String getReadyForecastCoordinate(double lat, double lot) throws RemoteException {
        return openWeatherMapJsonParser.getReadyForecastCoordinate(lat, lot);
    }

    @Override
    public String getReadyForecastById(int cityId) throws RemoteException {
//        System.out.println(openWeatherMapJsonParser.getReadyForecastById(cityId));
        return openWeatherMapJsonParser.getReadyForecastById(cityId);
    }

    @Override
    public String getReadyForecastWithThreeHourStep(String userName) throws RemoteException {
        String city = sql.getCityByUserName(userName);
        return openWeatherMapJsonParser.getReadyForecastWithThreeHourStep(city);
    }

    @Override
    public String getSubscribeTimeByUserName(String userName) throws RemoteException {
//        String subscribeTime = sql.getSubscribeTimeByUserName(userName);
//        SimpleDateFormat format = new SimpleDateFormat();
//        format.applyPattern("HH:mm");
//        Date date = new Date();
//        if(subscribeTime != null && !subscribeTime.isEmpty()){
//            date  = format.parse(subscribeTime);
//        }
        return sql.getSubscribeTimeByUserName(userName);
    }

    @Override
    public String addSubscribe(String userName, String cityName, String subscribeTime) throws RemoteException {
        boolean b = sql.checkSubscribe(userName);
        if(b == true){
            sql.replaceSubscribeTime(userName, cityName, subscribeTime);
        }
        else{
            incrementSubscribe();
            sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime));
        }
        return null;
    }

    public void incrementSubscribe(){
        listSubscribe = sql.checkSubscribeId();
        if(listSubscribe != null && !listSubscribe.isEmpty()){
            countSubscribe = listSubscribe.get(listSubscribe.size()-1);
            countSubscribe++;
        }
        else {
            countSubscribe++;
        }
    }

    public void incrementAuthorization(){
        listUsers = sql.checkUserId();
        if(listUsers != null && !listUsers.isEmpty()){
            countAuthorization = listUsers.get(listUsers.size()-1);
            countAuthorization++;
        }
        else{
            countAuthorization++;
        }
    }
}
