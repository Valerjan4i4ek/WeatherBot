import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscribeCache {
    MySQLClass sql = new MySQLClass();
    Map<Integer, Map<String, Map<String, String>>> subscribeCache = sql.getSubscribeCache();
    OpenWeatherMapJsonParser openWeatherMapJsonParser = new OpenWeatherMapJsonParser();
    List<Integer> listSubscribe;
    int countSubscribe;

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
}
