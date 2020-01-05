package com.vipvideo.pareser.service.impl;

import java.io.IOException;
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
public class TenXunTvSearch implements SearchService{
	@Value("${app.searchurl.tengxuntv}")
	String tengxunUrl;
	
	@Override
	public List<Object> searchMovie(String moviename) {
		
		try {
			
			List<Object> list=new ArrayList<Object>();
			Document document = Jsoup.connect(tengxunUrl+moviename).get();
			//中间整体
			Element wrapperMain =document.selectFirst("div.wrapper_main");
			//每个竖直图片
			Elements resultItems=wrapperMain.select("div.result_item.result_item_v");
			for(Element item:resultItems) {
				//名字
				//System.out.println(item.selectFirst("div._infos").selectFirst("h2.result_title").selectFirst("a").text());
				//地址
				Element playlist = item.selectFirst("div._playlist");
				Map<String,Object> childmap=new HashMap<>();
				
						if(playlist!=null) {
							Element result_btn_line=playlist.selectFirst("div.result_btn_line");
							if(result_btn_line!=null) {
								Element aelemtnElement=result_btn_line.selectFirst("a.btn_primary.btn_primary_lg");
								Map<String,Object> childmap3=new HashMap<>();
								childmap3.put("seriesName", aelemtnElement.text());
								childmap3.put("seriesHref", aelemtnElement.attr("href"));
								childmap.put("movieName", item.selectFirst("div._infos").selectFirst("h2.result_title").selectFirst("a").text());
								childmap.put("movieData", childmap3);
								list.add(childmap);
							}
						}
				//多级地址
				Element playlistElements= item.selectFirst("div._playlist");
				if(playlistElements!=null) {
					Elements itemElements= playlistElements.select("div.item");
					if(itemElements!=null&&itemElements.size()>0) {
						Map<String, Object> childmap2=new HashMap<>();
						List<Object> chiList=new ArrayList<Object>();
						for(Element itemli:itemElements) {
							Map<String,Object> childmap4=new HashMap<>();
							childmap4.put("seriesName", itemli.selectFirst("a").text());
							childmap4.put("seriesHref", itemli.selectFirst("a").attr("href"));
							chiList.add(childmap4);
						}
						childmap2.put("movieName", item.selectFirst("div._infos").selectFirst("h2.result_title").selectFirst("a").text());
						childmap2.put("movieData", chiList);
						list.add(childmap2);
					
							
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
