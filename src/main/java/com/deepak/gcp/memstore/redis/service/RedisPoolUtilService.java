package com.deepak.gcp.memstore.redis.service;

import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolUtilService {
	private static JedisPool jedisPool;
	
	public static JedisPool getJedisPoolService(String host,Integer port,Integer maxWaitMillis, Integer maxTotal) {
		if(jedisPool!= null) {
			return jedisPool;
		}
		jedisPool=creteJedisPoolService(host,port,maxWaitMillis,maxTotal);
		return jedisPool;
	}

	private static JedisPool creteJedisPoolService(String host, Integer port, Integer maxWaitMillis,
			Integer maxTotal) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxTotal(maxTotal);
		JedisPool pool = new JedisPool(jedisPoolConfig,host,port);
		return pool;
	}

}
