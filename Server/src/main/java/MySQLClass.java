import com.sun.rowset.CachedRowSetImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MySQLClass {

    public MySQLClass(){
        baseCreate();
        tableAuthorizationCreate();
        tableSubscribeCreate();
    }

    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String url = "jdbc:mysql://localhost/" + ((dbName != null)? (dbName) : (""));
        String username = "root";
        String password = "1234";
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        return DriverManager.getConnection(url, username, password);
    }

    public void baseCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection(null);
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
                        "(id INT NOT NULL, userName VARCHAR(20) NOT NULL, cityName VARCHAR(20) NOT NULL, subscribeTime VARCHAR(20) NOT NULL)");
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

    public void addSubscribe(Subscribe subscribe){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("WeatherBot");
                ps = conn.prepareStatement("INSERT INTO subscribe (id, userName, cityName, subscribeTime) VALUES (?, ?, ?, ?)");
                ps.setInt(1, subscribe.getId());
                ps.setString(2, subscribe.getUserName());
                ps.setString(3, subscribe.getCityName());
                ps.setString(4, subscribe.getSubscribeTime());
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

    public void replaceSubscribeTime(String userName, String cityName, String subscribeTime){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "UPDATE subscribe SET subscribeTime = ?, cityName = ? WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, subscribeTime);
                ps.setString(2, cityName);
                ps.setString(3, userName);
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

    public String getSubscribeTimeByUserName(String userName){
        String subscribeTime = "";
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
//            CachedRowSetImpl crs = null;

            try{
                conn = getConnection("WeatherBot");
                String query = "SELECT subscribeTime FROM subscribe WHERE userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();
//                crs = new CachedRowSetImpl();
//                crs.populate(rs);

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
//                try {
//                    if (crs != null) {
//                        crs.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
