public class Subscribe {
    private int id;
    private String userName;
    private String cityName;
    private String subscribeTime;

    public Subscribe(int id, String userName, String cityName, String subscribeTime) {
        this.id = id;
        this.userName = userName;
        this.cityName = cityName;
        this.subscribeTime = subscribeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
}
