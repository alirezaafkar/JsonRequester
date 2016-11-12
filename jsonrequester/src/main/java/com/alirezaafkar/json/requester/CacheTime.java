package com.alirezaafkar.json.requester;

/**
 * Created by Alireza Afkar on 12/11/16.
 */
public class CacheTime {
    private long cacheHitButRefreshed = 3 * 60 * 1000;
    private long cacheExpired = 24 * 60 * 60 * 1000;

    /**
     * in 3 minutes cache will be hit, but also refreshed on background
     * in 24 hours this cache entry expires completely
     */
    public CacheTime() {
        this.cacheHitButRefreshed = 3 * 60 * 1000;
        this.cacheExpired = 24 * 60 * 60 * 1000;
    }

    /**
     * @param cacheHitButRefreshed in given time, cache will be hit, but also refreshed on background
     * @param cacheExpired         in given time, this cache entry expires completely
     */
    public CacheTime(long cacheHitButRefreshed, long cacheExpired) {
        this.cacheHitButRefreshed = cacheHitButRefreshed;
        this.cacheExpired = cacheExpired;
    }

    public long getCacheHitButRefreshed() {
        return cacheHitButRefreshed;
    }

    public void setCacheHitButRefreshed(long cacheHitButRefreshed) {
        this.cacheHitButRefreshed = cacheHitButRefreshed;
    }

    public long getCacheExpired() {
        return cacheExpired;
    }

    public void setCacheExpired(long cacheExpired) {
        this.cacheExpired = cacheExpired;
    }
}
