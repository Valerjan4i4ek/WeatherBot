import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteWeatherBotServer implements WeatherBot{
    private final static String JSON_FILE_NAME = "Server/citylist.json";
    MySQLClass sql = new MySQLClass();
    OpenWeatherMapJsonParser openWeatherMapJsonParser = new OpenWeatherMapJsonParser();
    Map<Integer, Map<String, String>> authorizationCache = sql.getAuthorizationCache();
    Map<String, String> authorizationCacheInnerMap = getInnerMapAuthorization(authorizationCache);
    Map<Integer, Map<String, Map<String, String>>> subscribeCache = sql.getSubscribeCache();
    List<Integer> listUsers;
    List<Integer> listSubscribe;
    int countAuthorization;
    int countSubscribe;

    public Map<String, String> getInnerMapAuthorization(Map<Integer, Map<String, String>> map){
        Map<String, String> returnMap = new ConcurrentHashMap<>();
        for(Map.Entry<Integer, Map<String, String>> entry : map.entrySet()){
            returnMap.putAll(entry.getValue());
        }
        return returnMap;
    }

    private static List<CityData> jsonToCityData(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), CityData[].class));
    }

    @Override
    public String checkAuthorization(String login, String password) throws RemoteException {
        if(authorizationCache != null && !authorizationCache.isEmpty()){
            if(authorizationCacheInnerMap.containsKey(login) && authorizationCacheInnerMap.get(login).equals(password)){
                System.out.println("AUTHORIZATION IS OK");
                return "AUTHORIZATION IS OK";
            }
            else if(authorizationCacheInnerMap.containsKey(login) && !authorizationCacheInnerMap.get(login).equals(password)){
                System.out.println("INCORRECT PASSWORD");
                return "INCORRECT PASSWORD";
            }
            else{
                incrementAuthorization();
                authorizationCacheInnerMap.put(login, password);
                authorizationCache.put(countAuthorization, authorizationCacheInnerMap);
                sql.addAuthorization(new User(countAuthorization, login, password));
                System.out.println("NEW REGISTRATION");
                return "NEW REGISTRATION";
            }
        }
        else{
            incrementAuthorization();
            authorizationCacheInnerMap.put(login, password);
            authorizationCache.put(countAuthorization, authorizationCacheInnerMap);
            sql.addAuthorization(new User(countAuthorization, login, password));
            System.out.println("new registration");
            return "new registration";
        }


//        String s = sql.checkUser(login, password);
//        if(s != null && !s.isEmpty()){
//            if(s.equals("NEW REGISTRATION")){
//                System.out.println(s);
//                incrementAuthorization();
//                sql.addAuthorization(new User(countAuthorization, login, password));
//                return s;
//            }
//            else{
//
//                System.out.println(s);
//                return s;
//            }
//        }
//        else {
//            incrementAuthorization();
//            sql.addAuthorization(new User(countAuthorization, login, password));
//            System.out.println("new registration");
//            return "new registration";
//        }
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
        return openWeatherMapJsonParser.getReadyForecastById(cityId);
    }

    @Override
    public String getReadyForecastWithThreeHourStep(String userName) throws RemoteException {
        String city = "";
        if(subscribeCache != null && !subscribeCache.isEmpty()){
            for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
                for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
                    if(pair.getKey().equalsIgnoreCase(userName)){
                        for(Map.Entry<String, String> set : pair.getValue().entrySet()){
                            city = set.getKey();
                        }
                    }
                }
            }
        }
//        String city = sql.getCityByUserName(userName);
        return openWeatherMapJsonParser.getReadyForecastWithThreeHourStep(city);
    }

    @Override
    public String getSubscribeTimeByUserName(String userName) throws RemoteException {
        String subscribeTime = "";
        for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
            for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
                if(pair.getKey().equalsIgnoreCase(userName)){
                    for(Map.Entry<String, String> set : pair.getValue().entrySet()){
                        subscribeTime = set.getValue();
                    }
                }
            }
        }
        return subscribeTime;
//        return sql.getSubscribeTimeByUserName(userName);
    }

    @Override
    public String addSubscribe(String userName, String cityName, String subscribeTime) throws RemoteException {
        Map<String, String> set = new ConcurrentHashMap<>();
        Map<String, Map<String, String>> pair2 = new ConcurrentHashMap<>();

        if(subscribeCache != null && !subscribeCache.isEmpty()){
            for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
                for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
                    if(pair.getKey().equalsIgnoreCase(userName)){
                        set.put(cityName, subscribeTime);
                        pair2.put(userName, set);
                        subscribeCache.put(entry.getKey(), pair2);
                        sql.replaceSubscribeTime(userName, cityName, subscribeTime);
                    }
                }
            }
        }else{
            incrementSubscribe();
            set.put(cityName, subscribeTime);
            pair2.put(userName, set);
            subscribeCache.put(countSubscribe, pair2);
            sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime));
        }



//        boolean b = sql.checkSubscribe(userName);
//        if(b == true){
//            sql.replaceSubscribeTime(userName, cityName, subscribeTime);
//        }
//        else{
//            incrementSubscribe();
//            sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime));
//        }
        return "";
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
