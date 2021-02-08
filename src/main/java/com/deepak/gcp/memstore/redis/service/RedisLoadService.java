package com.deepak.gcp.memstore.redis.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

@Service
public class RedisLoadService {
	@Value("${gcp.memstore.redis.host}")
	private String redisHost;
	@Value("${gcp.memstore.redis.port}")
	private int redisPort;
	@Value("${gcp.memstore.redis.maxWaitMillis}")
	private int maxWaitMillis;
	@Value("${gcp.memstore.redis.maxTotal}")
	private int maxTotal;
	@Value("${gcp.memstore.redis.ttl}")
	private int ttl;
	private static JedisPool jedisPool;
	
	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		jedisPool = RedisPoolUtilService.getJedisPoolService(redisHost, redisPort, maxWaitMillis, maxTotal);
		loadToRedis();
	}

	@Scheduled(cron="0 0 0 ? * * *", zone="IST")
	private void loadToRedis() {
		jedisPool = RedisPoolUtilService.getJedisPoolService(redisHost, redisPort, maxWaitMillis, maxTotal);
		try(Jedis jedis= jedisPool.getResource()){
			/*
			 * Use Pipeline when large Data needs to be pushed.
			 * else use jedis.setex(key, seconds, value)
			 */
			Pipeline pipe = jedis.pipelined();
			String uuid = UUID.randomUUID().toString();
			pipe.setex(uuid, ttl, "Redis Demo");
			pipe.sync();
		}
	}
	
	
	
	

}
