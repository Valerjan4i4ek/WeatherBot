import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Client {
    public static final String UNIQUE_BINDING_NAME = "server.WeatherBot";
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Registry registry;
    static WeatherBot weatherBot;
    private static User user;

    static {
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 2732);
            weatherBot = (WeatherBot) registry.lookup(UNIQUE_BINDING_NAME);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NotBoundException, IOException {
        authorization();
    }

    public static void authorization() throws IOException, NotBoundException, RemoteException{
        System.out.println("Enter you login and password");
        String login = reader.readLine();
        String password = reader.readLine();
        String city = "";
        String weatherOrSubscribe = "";
        String cityOrCoordinate = "";
        String subscribeTime = "";
        double lat;
        double lot;

        user = new User(login, password);
        String result = weatherBot.checkAuthorization(login, password);
        System.out.println(result);
        if(result.equals("INCORRECT PASSWORD")){
            authorization();
        }
        else{
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(30000);
                        getSubscribeTimeByUserName(user.getUserName());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            System.out.println("Do u wanna check weather or make subscribe? Choose w or s:");
            weatherOrSubscribe = reader.readLine();
            if(weatherOrSubscribe.equalsIgnoreCase("w")){
                System.out.println("choose city or add coordinate: c or k");
                cityOrCoordinate = reader.readLine();
                if(cityOrCoordinate.equalsIgnoreCase("c")){
                    System.out.println("for example: \"Dnipro\" or \"manchester\"");
                    city = reader.readLine();
                    getReadyForecast(city);
                }
                else if(cityOrCoordinate.equalsIgnoreCase("k")){
                    System.out.println("for example: \"48.0000\" or \"51.00\"");
                    lat = Double.parseDouble(reader.readLine());
                    lot = Double.parseDouble(reader.readLine());
                    getReadyForecastCoordinate(lat, lot);
                }
            }
            else if(weatherOrSubscribe.equalsIgnoreCase("s")){
                System.out.println();
                System.out.println("add time when u wanna get weather and city");
                subscribeTime = reader.readLine();
                city = reader.readLine();
                addSubscribe(login, city, subscribeTime);
            }

            System.out.println();

        }
    }

    public static void getSubscribeTimeByUserName(String userName) throws RemoteException {
        String subscribeTimeByUserName = weatherBot.getSubscribeTimeByUserName(userName);
        String forecast = weatherBot.getReadyForecastWithThreeHourStep(userName);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(System.currentTimeMillis());
        if(subscribeTimeByUserName.equalsIgnoreCase(currentTime)){
            System.out.println(forecast);
        }
//        while (true){
//            if(subscribeTimeByUserName.equalsIgnoreCase(currentTime)){
//                System.out.println(forecast);
//            }
//            break;
//        }
    }

    public static void getReadyForecast(String city) throws IOException{
        List<CityData> list = weatherBot.sameNameCitiesCount(city);
        Map<Integer, Integer>  checkedMap = new LinkedHashMap<>();
        int count = 1;
        int cityNumber = 0;
        String id;

        if(list.size() == 1){
            String s = weatherBot.getReadyForecast(city);
            System.out.println("weather in " + s);
        }
        else if(list.size() > 1){
            for (CityData cityData : list) {
                System.out.print(count + " ");
                System.out.println(cityData.getId() + " " + cityData.getName() + " " + cityData.getCountry());
                checkedMap.put(cityData.getId(), count);
                count++;
            }
            System.out.println("choose your city (number)");
            cityNumber = Integer.parseInt(reader.readLine());
            for(Map.Entry<Integer, Integer> pair: checkedMap.entrySet()){
                if(pair.getValue()==cityNumber){
                    id = weatherBot.getReadyForecastById(pair.getKey());
                    System.out.println(id);
                }
            }
        }
    }

    public static void getReadyForecastCoordinate(double lat, double lot) throws RemoteException{
        String s = weatherBot.getReadyForecastCoordinate(lat, lot);
        System.out.println(s);
    }

    public static void addSubscribe(String userName, String cityName, String subscribeTime) throws IOException {
        List<CityData> list = weatherBot.sameNameCitiesCount(cityName);
        Map<Integer, Integer>  checkedMap = new LinkedHashMap<>();
        int count = 1;
        int cityNumber = 0;
        String id;
        int cityId = 0;
        if(list.size() == 1){
            cityId = list.get(list.size()-1).getId();
            Map<Subscribe, Integer> s = weatherBot.addSubscribe(cityId, userName, cityName, subscribeTime);
        }
        else if(list.size() > 1){
            for (CityData cityData : list) {
                System.out.print(count + " ");
                System.out.println(cityData.getId() + " " + cityData.getName() + " " + cityData.getCountry());
                checkedMap.put(cityData.getId(), count);
                count++;
            }
            System.out.println("choose your city (number)");
            cityNumber = Integer.parseInt(reader.readLine());
            for(Map.Entry<Integer, Integer> pair: checkedMap.entrySet()){
                if(pair.getValue() == cityNumber){
                    cityId = pair.getKey();
                    Map<Subscribe, Integer> s = weatherBot.addSubscribe(cityId, userName, cityName, subscribeTime);
//                    id = weatherBot.getReadyForecastById(pair.getKey());
//                    cityId = pair.getKey();
//                    System.out.println(id);
//                    String s = weatherBot.addSubscribe(cityId, userName, cityName, subscribeTime);
                    System.out.println(s);
                }
            }
        }
//        String s = weatherBot.addSubscribe(cityId, userName, cityName, subscribeTime);

    }
}
