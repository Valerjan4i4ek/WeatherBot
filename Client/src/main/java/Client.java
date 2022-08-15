import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
            System.out.println();
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
                System.out.println("add time when u wanna get weather");
                subscribeTime = reader.readLine();
                addSubscribe(login, subscribeTime);
            }
        }
    }

    public static void getReadyForecast(String city) throws RemoteException, FileNotFoundException {
//        String s = weatherBot.getReadyForecast(city);
//        System.out.println("weather in " + s);
        Map<Integer, Map<String, String>> map = weatherBot.sameNameCitiesCount(city);
        if(map.size() == 1){
            String s = weatherBot.getReadyForecast(city);
            System.out.println("weather in " + s);
        }
        else if(map.size() > 1){
            for(Map.Entry<Integer, Map<String, String>> entry : map.entrySet()){
                for(Map.Entry<String, String> pair : entry.getValue().entrySet()){
                    System.out.println(pair.getKey() + " " + pair.getValue());
                }
            }
        }
    }

    public static void getReadyForecastCoordinate(double lat, double lot) throws RemoteException{
        String s = weatherBot.getReadyForecastCoordinate(lat, lot);
        System.out.println(s);
    }

    public static void addSubscribe(String userName, String subscribeTime) throws RemoteException {
        String s = weatherBot.addSubscribe(userName, subscribeTime);
    }
}