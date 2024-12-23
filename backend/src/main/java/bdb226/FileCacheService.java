package bdb226;

import net.rubyeye.xmemcached.MemcachedClient;
import java.util.concurrent.TimeoutException;

public class FileCacheService {
    private final MemcachedClient memcachedClient;

    public FileCacheService(MemcachedClient client) {
        if (client == null) {
            throw new IllegalArgumentException("Memcached client cannot be null");
        }
        this.memcachedClient = client;
    }

    /**
     * Caches the file data in Memcached.
     *
     * @param fileId         The ID of the file to cache.
     * @param base64FileData The Base64-encoded file data.
     * @param expiry         Expiry time in seconds.
     */
    public void cacheFile(String fileId, String base64FileData, int expiry) throws Exception {
        if (fileId == null || base64FileData == null) {
            throw new IllegalArgumentException("fileId and base64FileData cannot be null");
        }
        memcachedClient.set(fileId, expiry, base64FileData);
    }

    /**
     * Retrieves the cached file data.
     *
     * @param fileId The ID of the file to retrieve.
     * @return The Base64-encoded file data, or null if not found.
     */
    public String getCachedFile(String fileId) throws Exception {
        if (fileId == null) {
            throw new IllegalArgumentException("fileId cannot be null");
        }
        return memcachedClient.get(fileId);
    }
}
