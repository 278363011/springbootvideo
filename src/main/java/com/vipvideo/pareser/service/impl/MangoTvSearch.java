package com.vipvideo.pareser.service.impl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vipvideo.pareser.service.SearchService;

//@Service
public class MangoTvSearch implements SearchService{
	
	@Value("${app.searchurl.mangguotv}")
	String mangoUrl;
	

	@Override
	public String searchMovie(String moviename) {
		try {
			Document document = Jsoup.connect(mangoUrl+moviename).get();
			Elements resultlist =document.select("div.search-resultlist");
			if(resultlist.size()==1) {
				//单级
				Elements ads=resultlist.select("a.so-result-btn.play.report-click.report-action");
				for(Element ass:ads) {
					System.out.println(ass.attr("href"));
				}
			}else {
				//多级
				for (Element resultitem : resultlist) {
					Elements postCards=resultitem.select("div.so-result-info.search-movie.clearfix");
					for(Element post:postCards) {
						Elements as =post.select("a.so-result-btn.play.report-click.report-action");
						if(as.size()!=0) {
							System.out.println("链接："+as.attr("href"));
						}
					}
				}
				
				//多级
				for (Element resultitem : resultlist) {
					Elements postCards=resultitem.select("div.so-result-info.search-television.clearfix");
					for(Element post:postCards) {
						Element p =post.selectFirst("p.so-result-alist.playSeriesList");
						System.out.println(p.toString());
						for(Element a:p.select("a.report-click.report-action")) {
							System.out.println("链接:"+a.attr("href"));
						}
					}
				}
				
				
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
