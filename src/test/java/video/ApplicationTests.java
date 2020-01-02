package video;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.vipvideo.pareser.ParserAppliction;
import com.vipvideo.pareser.service.SearchService;

@RunWith(SpringRunner.class)   
@SpringBootTest(classes={ParserAppliction.class})// 指定启动类
public class ApplicationTests {
	@Autowired
	SearchService searchService;
		@Test
	    public void testMango(){
			searchService.searchMovie("少年的你");
	    }

}
