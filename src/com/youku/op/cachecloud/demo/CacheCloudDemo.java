package com.youku.op.cachecloud.demo;

import com.youku.op.builder.ClientBuilder;
import com.youku.op.builder.RedisClusterBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCluster;
import com.youku.op.cachecloud.client.basic.util.ConstUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheCloudDemo {
	private String appToken = "";
	
	private JedisCluster jedisCluster = null;
	private RedisClusterBuilder redisCluster = null;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public CacheCloudDemo(String appToken) {
		this.appToken = appToken;
	}
	
	public JedisCluster getJedisCluster() {
		if (this.jedisCluster == null) {
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
			poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
			poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
			poolConfig.setMaxWaitMillis(1000L);
			poolConfig.setJmxEnabled(true);
        
			try {

				redisCluster = ClientBuilder.redisCluster(appToken);

				redisCluster.setJedisPoolConfig(poolConfig);
				redisCluster.setConnectionTimeout(2000);
				this.jedisCluster = redisCluster.build();
			} catch (Exception e) {
				
				throw e;
			}
		}
		
		return this.jedisCluster;
        
	}
	
	public String getToken() {
		return this.appToken;
	}
	
	public void setValue(String key, String value) {
		this.getJedisCluster().set(key, value);
	}
	
	public String getValue(String key) {
		return this.getJedisCluster().get(key);
	}
	
	/*public static void main(String[] args) {
		int i = 0;
		String key, value;
		
		System.out.println("use token = "+ConstUtils.CLIENT_TOKEN);
		CacheCloudDemo demo = new CacheCloudDemo(ConstUtils.CLIENT_TOKEN);
		

		for (i = 0; i< 100; i++) {
			try {
				key = "foo"+i;
				value = "value"+i;
				demo.setValue(key, value);
				value = demo.getValue(key);
				System.out.println("sucess! key = "+key+", value = "+value);
			} catch (Exception e) {
				throw e;
			}
		}
	}*/

}
