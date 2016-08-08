package com.searchengine.webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private Queue<URL> links = new LinkedList<URL>();
	private final static Logger logger = Logger.getLogger(Crawler.class.getName());

	public Queue<URL> crawl(URL page) throws IOException {
		Connection connection = Jsoup.connect(page.toString()).userAgent(USER_AGENT);
		Document htmlDocument = null;
		try {
			htmlDocument = connection.get();
		} catch (IOException e) {
			logger.info(e.toString());
			throw e;
		}

		if (connection.response().statusCode() == 200)
			System.out.println("\nReceived web page " + page.toString());

		if (!connection.response().contentType().contains("text/html")) {
			logger.info("Currently only parses HTML pages.");
			throw new IOException("Currently only parses HTML pages.");
		}
		Elements linksOnPage = htmlDocument.select("a[href]");

		System.out.println("Crawled " + linksOnPage.size() + " links");

		for (Element link : linksOnPage) {
			try {
				System.out.println(link.absUrl("href"));
				links.add(new URL(link.absUrl("href")));
			} catch (MalformedURLException e) {
				logger.info(e.toString());
				throw e;
			}
		}

		return links;
	}

	public Queue<URL> getLinks() throws Exception {
		if (links.size() == 0)
			throw new Exception("Please call crawl method first, before fetching links for the page.");
		return links;
	}
}
