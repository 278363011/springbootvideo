package com.vipvideo.pareser.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.vipvideo.pareser.service.SearchService;

@RestController
public class SearchParseController {

	@Autowired
	Map<String,SearchService> searchServiceList;
	
	
	@RequestMapping("search")
	public Map<String, Object> search(String searchTeString){
		int num=searchServiceList.size();
		System.out.println("服务数"+num);
		ExecutorService exec = Executors.newFixedThreadPool(num);
		final CountDownLatch countDownLatch = new CountDownLatch(num);
		Map<String, Object> resultMap=new HashMap<>();
		for(Entry<String,SearchService> entry:searchServiceList.entrySet()) {
			FutureTask<String> futureTaskCallable = new FutureTask<>(new Callable<String>(){
				@Override
				public String call() throws Exception {
					//具体操作
					System.out.println(entry.getKey()+"子线程启动.....");
					   try {
						   resultMap.put(entry.getKey(), entry.getValue().searchMovie(searchTeString));
						   return "sucess";
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    return "error";
		                } finally {
		                    countDownLatch.countDown();
		                }
				}
			});
			exec.submit(futureTaskCallable);
		}
        try {
			countDownLatch.await();
			System.out.println("所有子线程执行完毕，返回最终结果======================>");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			 System.out.println("all finished!!!");
		     exec.shutdown();
        }
		return resultMap;
       
	}
}
