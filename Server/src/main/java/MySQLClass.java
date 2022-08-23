import com.sun.rowset.CachedRowSetImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLClass {
    private final static String fileName = "database.properties";

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySQLClass(){
//        init();
        baseCreate();
        tableAuthorizationCreate();
        tableSubscribeCreate();
    }

//    public static void loadDriver(){
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
////            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
//        }  catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//    }

    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        String url = "jdbc:mysql://localhost/" + ((dbName != null)? (dbName) : (""));
//        String username = "root";
//        String password = "1234";
//        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        Properties props = new Properties();
        try(InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)){
            if(in != null){
                props.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String driver = props.getProperty("driver");

        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

//        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        
        return DriverManager.getConnection(url, username, password);
    }

    public void baseCreate(){

        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("WeatherBot");
                st = conn.createStatement();
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS WeatherBot");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableSubscribeCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("WeatherBot");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS WeatherBot.subscribe " +
                        "(id INT NOT NULL, userName VARCHAR(20) NOT NULL, cityId INT NOT NULL, cityName VARCHAR(20) NOT NULL, subscribeTime VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableAuthorizationCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("WeatherBot");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS WeatherBot.authorization " +
                        "(id INT NOT NULL, login VARCHAR(20) NOT NULL, password VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addSubscribe(Subscribe subscribe, int cityId){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("WeatherBot");
                ps = conn.prepareStatement("INSERT INTO subscribe (id, userName, cityId, cityName, subscribeTime) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, subscribe.getId());
                ps.setString(2, subscribe.getUserName());
                ps.setInt(3, cityId);
                ps.setString(4, subscribe.getCityName());
                ps.setString(5, subscribe.getSubscribeTime());
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void replaceSubscribeTime(String userName, int cityId, String cityName, String subscribeTime){

        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "UPDATE subscribe SET subscribeTime = ?, cityId = ?, cityName = ? WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, subscribeTime);
                ps.setInt(2, cityId);
                ps.setString(3, cityName);
                ps.setString(4, userName);
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addAuthorization(User user){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("WeatherBot");
                ps = conn.prepareStatement("INSERT INTO authorization (id, login, password) VALUES (?, ?, ?)");
                ps.setInt(1, user.getId());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getUserPassword());
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getCityByUserName(String userName){
        String  city = "";
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT cityName FROM subscribe WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();

                while (rs.next()){
                    city = rs.getString("cityName");
                }
            }finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return city;
    }

    public List<Subscribe> getSubscribeUserCache(){
        List<Subscribe> list = new CopyOnWriteArrayList<>();
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT * FROM subscribe";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    String userName = rs.getString("userName");
                    int cityId = rs.getInt("cityId");
                    String cityName = rs.getString("cityName");
                    String subscribeTime = rs.getString("subscribeTime");

                    list.add(new Subscribe(id, userName, cityId, cityName, subscribeTime));
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

//    public Map<Subscribe, Integer> getSubscribeCache(){
//        Map<Subscribe, Integer> map = new ConcurrentHashMap<>();
//        try{
//            Connection conn = null;
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//
//            try{
//                conn = getConnection("WeatherBot");
//                String query = "SELECT * FROM subscribe";
//                ps = conn.prepareStatement(query);
//                rs = ps.executeQuery();
//
//                while (rs.next()){
//                    int id = rs.getInt("id");
//                    String userName = rs.getString("userName");
//                    String cityName = rs.getString("cityName");
//                    String subscribeTime = rs.getString("subscribeTime");
//                    int cityId = rs.getInt("cityId");
//                    map.put(new Subscribe(id, userName, cityName, subscribeTime), cityId);
//                }
//            }finally {
//                try {
//                    if (conn != null) {
//                        conn.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (ps != null) {
//                        ps.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return map;
//    }

//    public Map<Integer, Map<String, Map<String, String>>> getSubscribeCache(){
//        Map<Integer, Map<String, Map<String, String>>> map = new ConcurrentHashMap<>();
//        Map<String, Map<String, String>> firstInnerMap = new ConcurrentHashMap<>();
//        Map<String, String> secondInnerMap = new ConcurrentHashMap<>();
//        try{
//            Connection conn = null;
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//
//            try{
//                conn = getConnection("WeatherBot");
//                String query = "SELECT * FROM subscribe";
//                ps = conn.prepareStatement(query);
//                rs = ps.executeQuery();
//
//                while (rs.next()){
//                    int id = rs.getInt("id");
//                    String userName = rs.getString("userName");
//                    String cityName = rs.getString("cityName");
//                    String subscribeTime = rs.getString("subscribeTime");
//
//                    secondInnerMap.put(cityName, subscribeTime);
//                    firstInnerMap.put(userName, secondInnerMap);
//                    map.put(id, firstInnerMap);
//                }
//            }finally {
//                try {
//                    if (conn != null) {
//                        conn.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (ps != null) {
//                        ps.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return map;
//    }

    public List<User> getAuthorizationUserCache(){
        List<User> list = new CopyOnWriteArrayList<>();
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT * FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    String login = rs.getString("login");
                    String password = rs.getString("password");
                    list.add(new User(id, login, password));
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public Map<Integer, Map<String, String>> getAuthorizationCache(){
        Map<Integer, Map<String, String>> map = new ConcurrentHashMap<>();
        Map<String, String> innerMap = new ConcurrentHashMap<>();
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT * FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    String login = rs.getString("login");
                    String password = rs.getString("password");
                    innerMap.put(login, password);
                    map.put(id, innerMap);
                }
            }finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public String getSubscribeTimeByUserName(String userName){
        String subscribeTime = "";
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT subscribeTime FROM subscribe WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();

                while (rs.next()){
                    subscribeTime = rs.getString("subscribeTime");
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return subscribeTime;
    }

    public boolean checkSubscribe(String userName){
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT * FROM subscribe WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();

                if(rs.next()){
                    return userName.equals(rs.getString("userName"));
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public String checkUser(String login, String password) {
        try {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = getConnection("WeatherBot");
                String query = "SELECT * FROM authorization WHERE login = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, login);
                rs = ps.executeQuery();

                if (rs.next()) {
                    if(password.equals(rs.getString("password"))){
                        return  "AUTHORIZATION IS OK";
                    }
                    else{
                        return  "INCORRECT PASSWORD";
                    }
                }
                else{
                    return "NEW REGISTRATION";
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public List<Integer> checkSubscribeId(){
        List<Integer> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT id FROM subscribe";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    list.add(id);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Integer> checkUserId(){
        List<Integer> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT id FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    list.add(id);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
