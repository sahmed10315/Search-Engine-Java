package com.searchengine.webcrawler;
  
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CrawlWeb {

	private int maxPages;
	private int maxDepth;

	private Set<URL> crawled;
	private Queue<URL> toCrawl;

	public CrawlWeb(int depth, int pages) {
		this.maxDepth = depth;
		this.maxPages = pages;

		crawled = new HashSet<URL>();
		toCrawl = new LinkedList<URL>();
	}

	public Set<URL> crawlWeb(URL seedPage) throws IOException {
		Queue<URL> nextDepth = new LinkedList<URL>();
		toCrawl.add(seedPage);
		int depth = 1;
		Crawler crawler = new Crawler();
		
		while (!(toCrawl.isEmpty()) && depth <= maxDepth) {
			URL page = toCrawl.remove();

			if (!(crawled.contains(page)) && crawled.size() < maxPages) {
				nextDepth.addAll(crawler.crawl(page));
				crawled.add(page);
			}

			if (toCrawl.isEmpty()) {
				toCrawl = nextDepth;
				nextDepth = new LinkedList<URL>();
				depth++;
			}
		}
		
		System.out.println(crawled.size() + " pages crawled.\n");
		return crawled;
	}

	public static void main(String[] args) throws MalformedURLException, IOException {
		CrawlWeb crawlWeb = new CrawlWeb(5, 8);
		crawlWeb.crawlWeb(new URL("http://127.0.0.1"));
	}
}
