package video;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.vipvideo.pareser.ParserAppliction;
import com.vipvideo.pareser.service.SearchService;

@RunWith(SpringRunner.class)   
@SpringBootTest(classes={ParserAppliction.class})// 指定启动类
public class ApplicationTests {
	
	@Autowired
	Map<String,SearchService> searchServiceList;
		@Test
	    public void testMango(){
			String searchTeString="庆余年";
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
					System.out.println(JSON.toJSONString(resultMap));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		        System.out.println("all finished!!!");
		        exec.shutdown();
	    

}
}