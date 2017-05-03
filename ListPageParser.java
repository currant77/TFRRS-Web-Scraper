package Parsers;

import Model.Database;
import Model.Division;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Taylor Curran on 27/3/17.
 */
public class ListPageParser {

    private Database db;
    private String url;
    private Map<String, Division> urls;
    private QualifyingListParser qpp;
    private PrintWriter pw;

    public ListPageParser(String url, PrintWriter pw){
        this.url = url;
        this.urls = new HashMap<>();
        this.pw = pw;
    }

    public void parseUrls() throws IOException {

        Document doc = Jsoup.connect(url).maxBodySize(0).timeout(0).get();
        Element data = doc.getElementsByClass("data").first();
        Elements rows = data.getElementsByTag("tr");

        if (!rows.isEmpty()){
            for (int i = 0; i < rows.size(); i++){
                getEntry(rows.get(i));
            }
        }
    }

    public void parseQualifyingLists() throws ClassException, IDException, IOException {
        Set<String> qlpurls = urls.keySet();
        for (String url : qlpurls){
            qpp = new QualifyingListParser(url, urls.get(url), pw);
            qpp.parse();
        }
    }

    private void getEntry(Element row) {
        Elements entries = row.getElementsByTag("td");
        for (int i = 0; i <= 2; i++){
            Element entry = entries.get(i);
            getUrl(entry, intToDivision(i));
        }
    }

    // Effects: gets url and puts it in hashmap with given division
    private void getUrl(Element entry, Division d) {
        try{
            String url = entry.getElementsByAttribute("href").first().attr("href");
            url = QualifyingListParser.adjustUrl(url);
            urls.put(url,d);
        } catch (Exception e){
            return;
        }
    }

    // Required: i = 1,2,or 3
    // Effects: returns corresponding division
    private Division intToDivision(int i){
        if (i == 0) return Division.DIV_I;
        if (i == 1) return Division.DIV_II;
        else return Division.DIV_III;
    }

// For testing ==================================================================

    public Division getDiv(String url){
        return urls.get(url);
    }

    public int getsize(){return urls.size();}

    public boolean isEmpty(){return urls.isEmpty();}

}
