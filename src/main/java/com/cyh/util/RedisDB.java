package com.cyh.util;


import com.cyh.common.UploadConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDB {
	/*最大连接数*/
	private static int maxTotal = Integer.parseInt(UploadConfigurer.propMap.get("redis.maxTotal"));
	/*最小连接数*/
	private static int minIdle = Integer.parseInt(UploadConfigurer.propMap.get("redis.minIdle"));
	/*最大空闲等待数*/
	private static int maxIdle = Integer.parseInt(UploadConfigurer.propMap.get("redis.maxIdle"));
	/*最大等待时间*/
	private static int maxWaitMillis = Integer.parseInt(UploadConfigurer.propMap.get("redis.maxWaitMillis"));
	/*从pool中获取连接时，是否检查连接可用*/
	private static boolean testOnBorrow = Boolean.parseBoolean(UploadConfigurer.propMap.get("redis.testOnBorrow"));
	/*端口号*/
	private static int port = Integer.parseInt(UploadConfigurer.propMap.get("redis.port"));
	/*ip地址*/
	private static String hostName = UploadConfigurer.propMap.get("redis.hostName");
	/*redis连接密码*/
	private static String password = UploadConfigurer.propMap.get("redis.password");

	/*是否对空闲连接对象进行检查
	private static boolean testOnIdle = Configuration.propMap.get("redis.testOnIdle").equalsIgnoreCase("true")?true:false;
	每隔多少秒检查一次空闲连接对象
	private static int timeBetweenEvictionRunsMillis = Integer.valueOf(Configuration.propMap.get("redis.timeBetweenEvictionRunsMillis"));
	一次驱逐过程中最多驱逐对象的个数
	private static int numTestsPerEvictionRun = Integer.parseInt(Configuration.propMap.get("redis.numTestsPerEvictionRun"));
	表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
	private static int minEvictableIdleTimeMillis = Integer.parseInt(Configuration.propMap.get("redis.minEvictableIdleTimeMillis"));*/

	/*连接超时*/
	private static int timeout = Integer.parseInt(UploadConfigurer.propMap.get("redis.timeout"));
	/*聊天记录存放在第一个redis库*/
	public static int dbSelectedForHistoryMessage = Integer.parseInt(UploadConfigurer.propMap.get("redis.dbSelectedForHistoryMessage"));
	/*系统相关信息存放第二个redis库*/
	public static int dbSelectedForSystem = Integer.parseInt(UploadConfigurer.propMap.get("redis.dbSelectedForSystem"));
	/*所有系统用户再存入redis时的key*/
	public static String systemUsers = UploadConfigurer.propMap.get("redis.systemUsers");

	/*jedis连接池对象*/
	private static JedisPool jedisPool;

	/**
	 * 初始化连接池
	 * */
	public synchronized static void init(){
		try{
			JedisPoolConfig config=new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);//最小空闲连接数
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow(testOnBorrow);
			jedisPool = new JedisPool(config, hostName, port, timeout, password);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
   * 获取Jedis实例
   * @return
   */
	public synchronized static Jedis getJedis(){
		try{
			if(jedisPool == null){
				init();
			}
			try {
				Jedis Jedis = jedisPool.getResource();
				return Jedis;
			}catch (Exception e){
				e.printStackTrace();
				init();
			}
			return jedisPool.getResource();
		}catch(Exception e){
			e.printStackTrace();
			init();
		}
		return jedisPool.getResource();
	}

	/**
	 *释放jedis资源
	 *@paramjedis
	 */
	public synchronized static void returnResource(Jedis jedis){
		if(jedis!=null){
			jedis.close();
		}
	}

	/**
	 * Jedis对象出异常的时候，回收Jedis对象资源
	 *
	 * @param jedis
	 */
	public synchronized static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}

	}
}