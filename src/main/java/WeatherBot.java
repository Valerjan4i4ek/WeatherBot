import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface WeatherBot extends Remote{
    String checkAuthorization(String login, String password) throws RemoteException;
    String getReadyForecast(String city) throws RemoteException, FileNotFoundException;
    String getReadyForecastCoordinate(double lat, double lot) throws RemoteException;
    String addSubscribe(String userName, String subscribeTime) throws RemoteException;
    Map<Integer, Map<String, String>> sameNameCitiesCount(String cityName) throws RemoteException, FileNotFoundException;
}
