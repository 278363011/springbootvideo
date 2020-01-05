package com.vipvideo.pareser.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.vipvideo.pareser.service.SearchService;

@Service
public class AiqiyiSearch implements SearchService{
	
	@Value("${app.searchurl.aiqiyi}")
	String aiqiyiUrl;

	@Override
	public List<Object> searchMovie(String moviename) {
		
		try {
			
			List<Object> list=new ArrayList<Object>();
			Document document = Jsoup.connect(aiqiyiUrl+moviename).get();
			//整个左边部分
			Element layoutmain =document.selectFirst("div.layout-main");
			//每个竖直图片
			Elements verticalPic=layoutmain.select("div.qy-search-result-item.vertical-pic");
				for(Element picbox:verticalPic) {
					//多级
					if(!picbox.attr("data-searchpingback-albumname").trim().equals("")) {
						//本身影名
//						System.out.println(picbox.attr("data-searchpingback-albumname"));
						//右边级数部分
						Element rightResult =picbox.selectFirst("div.result-right");
						Elements listElements=rightResult.getElementsByAttributeValue("data-tvlist-elem", "alllist");
						
						if(listElements!=null&&listElements.size()>0) {
							//右边级数下面找具体的等级信息(单级)
							Element aElement=listElements.get(0).selectFirst("a.qy-search-result-btn");
							if(aElement!=null) {
								Map<String,Object> childmap=new HashMap<>();
								Map<String,String> childmap3=new HashMap<>();
								childmap3.put("seriesName", aElement.text());
								childmap3.put("seriesHref", aElement.attr("href"));
								
								childmap.put("movieName", picbox.attr("data-searchpingback-albumname"));
								childmap.put("movieData", childmap3);
								if(childmap!=null&&childmap.size()>0) {
									list.add(childmap);
								}
							}
							//右边级数下面找具体的等级信息(多级)
							Elements liListElements=listElements.get(0).select("li");
							Map<String, Object> childmap2=new HashMap<>();
							List<Object> chiList=new ArrayList<Object>();
							for(Element li:liListElements) {
								Map<String,Object> childmap4=new HashMap<>();
								childmap4.put("seriesName", li.selectFirst("a").attr("title"));
								childmap4.put("seriesHref", li.selectFirst("a").attr("href"));
								chiList.add(childmap4);
							}
							if(chiList.size()>0) {
								childmap2.put("movieName", picbox.attr("data-searchpingback-albumname"));
								childmap2.put("movieData", chiList);
							}
							if(childmap2!=null&&childmap2.size()>0) {
								list.add(childmap2);
							}
						}
					}
				}
				
				return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
