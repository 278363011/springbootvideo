package com.vipvideo.pareser.service.impl;

import java.io.IOException;

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
	public String searchMovie(String moviename) {
		
		try {
			Document document = Jsoup.connect(aiqiyiUrl+moviename).get();
			Element layoutmain =document.selectFirst("div.layout-main");
			Elements box=layoutmain.select("div.qy-search-result-item.vertical-pic");
				for(Element cbox:box) {
					//多级
					if(!cbox.attr("data-searchpingback-albumname").equals("")) {
						//电影名
						System.out.println("电影名"+cbox.attr("data-widget-searchlist-tvname"));
						Element urlElement2=cbox.selectFirst("div.qy-search-result-album");
						if(urlElement2!=null) {
							Elements ulElment=	urlElement2.select("ul.album-list");
							if(ulElment!=null) {
								for(Element ul:ulElment) {
									if(ul.attr("data-tvlist-elem").equals("list")){
										for(Element ae:ul.select("a")) {
											System.out.println("级数"+ae.attr("title"));
											System.out.println("地址"+ae.attr("href"));
										}
									}
								}
							}
						}
					
						
					}
				}
				for(Element pbox:box) {
					System.out.println("电影名"+pbox.attr("data-widget-searchlist-tvname"));
					Element urlElement=pbox.selectFirst("a.qy-search-result-btn");
					System.out.println("地址："+urlElement.attr("href"));
				}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
