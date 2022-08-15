public interface WeatherParser {
    String getReadyForecast(String city);
    String getReadyForecastCoordinate(double lat, double lot);
}
