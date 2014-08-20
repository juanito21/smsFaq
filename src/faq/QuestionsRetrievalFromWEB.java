package faq;

import static functions.CommonFunctions.log;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db.DataQueryHandler;
import db.DataUpdateHandler;
import db.Database;

public class QuestionsRetrievalFromWEB {

	private String url;
	private int index;
	private int limit;
	private Document doc;
	private static String TAG = "QuestionsRetrievalFromWEB";
	private static String BASE_URL = "http://www.healthcaremagic.com";
	private static String START_URL = "/community/Child-Health/12/";
	
	private static String DOMAIN = "health";
	private static String[] TAGS = {"child health", "baby", "children"};
	
	private static int COUNT = 0;
	private DataQueryHandler dqh;
	private DataUpdateHandler duh;
	
	public QuestionsRetrievalFromWEB(Database myDB) {
		url = BASE_URL + START_URL;
		index = 0;
		limit = 10000;
		dqh = new DataQueryHandler(myDB);
		duh = new DataUpdateHandler(myDB);
	}
	
	public void init() {
		log(TAG, "Initializing...");
		try {
			log(TAG, "Reading at : " + url);
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			log(TAG, "An error was detected while reading an html file");
			e.printStackTrace();
		}
		log(TAG, "Initialized...");
	}
	
	public void getQuestions() {
		url = BASE_URL + START_URL + index;
		init();
		Elements questionsBox = doc.select("div[class=questionBox linePadding10]");
		for(Element qBox : questionsBox) {
			String nbAnswer = qBox.select("td[width=15%]").first().text();
			if(!nbAnswer.equals("0")) {
				log(TAG, "OK ANSWER");
				Elements questions = qBox.select("a[class]");
				for(Element q : questions) {
					String qText = q.text().toLowerCase();
					int size = qText.length();
					int idQ = -1;
					if(qText.substring(size-1, size).equals("?")) {
						if(!dqh.isQuestionExists(qText)) idQ = duh.addQuestion(qText);
						log(TAG, COUNT + " : " + q.text());
						String urlAnswer = q.attr("href");
						log(TAG, "Go to : " + urlAnswer);
						url = BASE_URL + urlAnswer;
						init();
						getAnswer(idQ);
						COUNT++;
					}
				}
			}
		}
	}
	
	public void getAnswer(int idQ) {
		Element p = doc.select("div[id^=answerText]").first();
		if(p!= null && idQ>0) {
			int idA = duh.addAnswer(p.text());
			duh.addQuestionAnswer(idQ, idA);
			log(TAG, "Answer : " + p.text());
		}
	}
	
	public boolean next() {
		if(index<limit) {
			index+=20;
			return true;
		} return false;
	}
	
	public void retrieve() {
		getQuestions();
		while(next()) {
			getQuestions();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
