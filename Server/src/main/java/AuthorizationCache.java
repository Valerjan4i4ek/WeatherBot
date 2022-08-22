import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorizationCache {
    MySQLClass sql = new MySQLClass();
    Map<Integer, Map<String, String>> authorizationCache = sql.getAuthorizationCache();
    Map<String, String> authorizationCacheInnerMap = getInnerMapAuthorization(authorizationCache);
    List<Integer> listUsers;
    int countAuthorization;

    public Map<String, String> getInnerMapAuthorization(Map<Integer, Map<String, String>> map){
        Map<String, String> returnMap = new ConcurrentHashMap<>();
        for(Map.Entry<Integer, Map<String, String>> entry : map.entrySet()){
            returnMap.putAll(entry.getValue());
        }
        return returnMap;
    }

    public String checkAuthorization(String login, String password)  {
        if(authorizationCache != null && !authorizationCache.isEmpty()){
            if(authorizationCacheInnerMap.containsKey(login) && authorizationCacheInnerMap.get(login).equals(password)){
                System.out.println("AUTHORIZATION IS OK");
                return "AUTHORIZATION IS OK";
            }
            else if(authorizationCacheInnerMap.containsKey(login) && !authorizationCacheInnerMap.get(login).equals(password)){
                System.out.println("INCORRECT PASSWORD");
                return "INCORRECT PASSWORD";
            }
            else{
                incrementAuthorization();
                authorizationCacheInnerMap.put(login, password);
                authorizationCache.put(countAuthorization, authorizationCacheInnerMap);
                sql.addAuthorization(new User(countAuthorization, login, password));
                System.out.println("NEW REGISTRATION");
                return "NEW REGISTRATION";
            }
        }
        else{
            incrementAuthorization();
            authorizationCacheInnerMap.put(login, password);
            authorizationCache.put(countAuthorization, authorizationCacheInnerMap);
            sql.addAuthorization(new User(countAuthorization, login, password));
            System.out.println("new registration");
            return "new registration";
        }

    }

    public void incrementAuthorization(){
        listUsers = sql.checkUserId();
        if(listUsers != null && !listUsers.isEmpty()){
            countAuthorization = listUsers.get(listUsers.size()-1);
            countAuthorization++;
        }
        else{
            countAuthorization++;
        }
    }

    //    List<User> authorizationCache = sql.getAuthorizationCacheUser();

    //    public String checkAuthorization(String login, String password)  {
//        if(authorizationCache != null && !authorizationCache.isEmpty()){
//            for (int i = 0; i < authorizationCache.size(); i++) {
//                if(authorizationCache.get(i).getUserName().equals(login) && authorizationCache.get(i).getUserPassword().equals(password)){
//                    System.out.println("AUTHORIZATION IS OK");
//                    return "AUTHORIZATION IS OK";
//                }
//                else if(authorizationCache.get(i).getUserName().equals(login) && !authorizationCache.get(i).getUserPassword().equals(password)){
//                    System.out.println("INCORRECT PASSWORD");
//                    return "INCORRECT PASSWORD";
//                }
//                else{
//                    incrementAuthorization();
//                    authorizationCache.add(new User(countAuthorization, login, password));
//                    sql.addAuthorization(new User(countAuthorization, login, password));
//                    System.out.println("NEW REGISTRATION");
//                    return "NEW REGISTRATION";
//                }
//            }
//        }
//        else{
//            incrementAuthorization();
//            authorizationCache.add(new User(countAuthorization, login, password));
//            sql.addAuthorization(new User(countAuthorization, login, password));
//            System.out.println("new registration");
//            return "new registration";
//        }
//        return "";
//    }

}
