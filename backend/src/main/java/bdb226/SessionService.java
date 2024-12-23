package bdb226;

import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.MemcachedClient;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SessionService {
    private MemcachedClient memcachedClient;

    public SessionService(MemcachedClient client) {
        if (client == null) {
            throw new IllegalArgumentException("Memcached client cannot be null");
        }
        this.memcachedClient = client;
    }

    public void storeSession(String sessionId, String userData, int expiry) throws Exception {
        memcachedClient.set(sessionId, expiry, userData);
    }

    public String getSession(String sessionId) throws Exception {
        return memcachedClient.get(sessionId);
    }

    public void deleteSession(String sessionId) throws Exception {
        memcachedClient.delete(sessionId);
    }

    public void shutdown() throws Exception {
        memcachedClient.shutdown();
    }
}
