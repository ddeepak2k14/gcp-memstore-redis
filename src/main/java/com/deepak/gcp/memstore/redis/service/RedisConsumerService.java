package com.deepak.gcp.memstore.redis.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisConsumerService {
	private String redisHost;
	private int redisPort;
	private int maxWaitMillis;
	private int maxTotal;
	private static JedisPool jedisPool;
	
	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		jedisPool = RedisPoolUtilService.getJedisPoolService(redisHost, redisPort, maxWaitMillis, maxTotal);
		pullFromRedis();
	}

	@Scheduled(cron="0 0 0 ? * * *", zone="IST")
	private void pullFromRedis() {
		jedisPool = RedisPoolUtilService.getJedisPoolService(redisHost, redisPort, maxWaitMillis, maxTotal);
		try(Jedis jedis= jedisPool.getResource()){
			String myKey= "Deepak";
			jedis.get(myKey);
		}
	}
}
