package net.etfbl.org.connection;

import net.etfbl.org.propertiesLoader.PropertiesFileLoader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisConnection {
	
	private static String address = PropertiesFileLoader.getInstance().getSpeciefiedProperty("jedis_address");
	private static int port = Integer.valueOf(PropertiesFileLoader.getInstance().getSpeciefiedProperty("jedis_port"));

	private static JedisPool JEDIS_POOL = null;
	
	
	private JedisConnection() {
		
	}
	
	public static Jedis getInstance() {
		if(JEDIS_POOL == null) {
			JEDIS_POOL = new JedisPool(address, port);	
		}
		
		return JEDIS_POOL.getResource();
	}
	
	public static void closePool() {
		if(JEDIS_POOL != null)
			JEDIS_POOL.close();
	}
}
