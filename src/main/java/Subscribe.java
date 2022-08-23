import java.io.Serializable;

public class Subscribe implements Serializable {
    private int id;
    private String userName;
    private int cityId;
    private String cityName;
    private String subscribeTime;

    public Subscribe(int id, String userName, int cityId, String cityName, String subscribeTime) {
        this.id = id;
        this.userName = userName;
        this.cityId = cityId;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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
