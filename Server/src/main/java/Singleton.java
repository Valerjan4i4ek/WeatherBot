import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Singleton {
    private static Singleton instance;

    private  final ConcurrentHashMap<String, MySQLClass> cache;

    public Map<String, MySQLClass> getCache() {
        return cache;
    }
    private Singleton() {
        this.cache = new ConcurrentHashMap<>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
