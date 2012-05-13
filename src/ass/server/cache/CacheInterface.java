package ass.server.cache;

public interface CacheInterface <Key,Value> {
	
	public boolean isObjectCached(Key key);
	
	public Value getCachedObject(Key key);
	
	public boolean cacheNewObject(Key key);
	
	public boolean removeFromCache(Value value);

}
