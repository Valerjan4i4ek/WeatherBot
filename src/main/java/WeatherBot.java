import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface WeatherBot extends Remote{
    String checkAuthorization(String login, String password) throws RemoteException;
    String getReadyForecast(String city) throws RemoteException, FileNotFoundException;
    String getReadyForecastCoordinate(double lat, double lot) throws RemoteException;
    String getReadyForecastById(int cityId) throws RemoteException;
    String getReadyForecastWithThreeHourStep(String userName) throws RemoteException;
    String getSubscribeTimeByUserName(String userName) throws RemoteException;
    Map<String, Subscribe> addSubscribe(int cityId, String userName, String cityName, String subscribeTime) throws RemoteException;
    List<CityData> sameNameCitiesCount(String cityName) throws RemoteException, FileNotFoundException;
}
