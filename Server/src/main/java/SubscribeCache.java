import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscribeCache {
    MySQLClass sql = new MySQLClass();
    Map<Subscribe, Integer> subscribeCache = sql.getSubscribeCache();
    Map<String, String> innerSubscribeMap = new ConcurrentHashMap<>();

//    Map<Integer, Map<String, Map<String, String>>> subscribeCache = sql.getSubscribeCache();
    OpenWeatherMapJsonParser openWeatherMapJsonParser = new OpenWeatherMapJsonParser();
    List<Integer> listSubscribe;
    int countSubscribe;

    public String getReadyForecastWithThreeHourStep(String userName) throws RemoteException {
        String city = "";
        if(subscribeCache != null && !subscribeCache.isEmpty()){
            for(Map.Entry<Subscribe, Integer> entry : subscribeCache.entrySet()){
                if(entry.getKey().getUserName().equalsIgnoreCase(userName)){
                    city = entry.getKey().getCityName();
                }
            }
        }
//        String city = sql.getCityByUserName(userName);
        return openWeatherMapJsonParser.getReadyForecastWithThreeHourStep(city);
    }

    public String getSubscribeTimeByUserName(String userName) throws RemoteException {
        String subscribeTime = "";
        for(Map.Entry<Subscribe, Integer> entry : subscribeCache.entrySet()){
            if(entry.getKey().getUserName().equalsIgnoreCase(userName)){
                subscribeTime = entry.getKey().getSubscribeTime();
            }
        }
        return subscribeTime;
//        return sql.getSubscribeTimeByUserName(userName);
    }

    public Map<String, String> getInnerMap(Map<Subscribe, Integer> map){
        Map<String, String> returnMap = new ConcurrentHashMap<>();
        for(Map.Entry<Subscribe, Integer> entry : map.entrySet()){
            returnMap.put(entry.getKey().getUserName(), entry.getKey().getCityName());
        }
        return returnMap;
    }


    public Map<Subscribe, Integer> addSubscribe(int cityId, String userName, String cityName, String subscribeTime) {
        if(subscribeCache != null && !subscribeCache.isEmpty()){
            innerSubscribeMap = getInnerMap(subscribeCache);
            if(innerSubscribeMap.containsKey(userName)){
                innerSubscribeMap.put(userName, cityName);
                subscribeCache.put(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
                sql.replaceSubscribeTime(userName, cityId, cityName, subscribeTime);
            }
            else{
                incrementSubscribe();
                subscribeCache.put(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
                sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
            }

//            for(Map.Entry<Subscribe, Integer> entry : subscribeCache.entrySet()){
//                if(entry.getKey().getUserName().equalsIgnoreCase(userName)){
//                    subscribeCache.put(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
//                    sql.replaceSubscribeTime(userName, cityId, cityName, subscribeTime);
//                    break;
//                }
//                else{
//                    incrementSubscribe();
//                    subscribeCache.put(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
//                    sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
//                    break;
//                }
//            }
        }else{
            incrementSubscribe();
            subscribeCache.put(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
            sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime), cityId);
        }

        return subscribeCache;
    }

//    public String getReadyForecastWithThreeHourStep(String userName) throws RemoteException {
//        String city = "";
//        if(subscribeCache != null && !subscribeCache.isEmpty()){
//            for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
//                for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
//                    if(pair.getKey().equalsIgnoreCase(userName)){
//                        for(Map.Entry<String, String> set : pair.getValue().entrySet()){
//                            city = set.getKey();
//                        }
//                    }
//                }
//            }
//        }
////        String city = sql.getCityByUserName(userName);
//        return openWeatherMapJsonParser.getReadyForecastWithThreeHourStep(city);
//    }

//    public String getSubscribeTimeByUserName(String userName) throws RemoteException {
//        String subscribeTime = "";
//        for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
//            for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
//                if(pair.getKey().equalsIgnoreCase(userName)){
//                    for(Map.Entry<String, String> set : pair.getValue().entrySet()){
//                        subscribeTime = set.getValue();
//                    }
//                }
//            }
//        }
//        return subscribeTime;
////        return sql.getSubscribeTimeByUserName(userName);
//    }

//    public String addSubscribe(String userName, String cityName, String subscribeTime) throws RemoteException {
//        Map<String, String> set = new ConcurrentHashMap<>();
//        Map<String, Map<String, String>> pair2 = new ConcurrentHashMap<>();
//
//        if(subscribeCache != null && !subscribeCache.isEmpty()){
//            for(Map.Entry<Integer, Map<String, Map<String, String>>> entry : subscribeCache.entrySet()){
//                for(Map.Entry<String, Map<String, String>> pair : entry.getValue().entrySet()){
//                    if(pair.getKey().equalsIgnoreCase(userName)){
//                        set.put(cityName, subscribeTime);
//                        pair2.put(userName, set);
//                        subscribeCache.put(entry.getKey(), pair2);
//                        sql.replaceSubscribeTime(userName, cityName, subscribeTime);
//                    }
//                }
//            }
//        }else{
//            incrementSubscribe();
//            set.put(cityName, subscribeTime);
//            pair2.put(userName, set);
//            subscribeCache.put(countSubscribe, pair2);
//            sql.addSubscribe(new Subscribe(countSubscribe, userName, cityName, subscribeTime));
//        }
//
//        return "";
//    }

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
