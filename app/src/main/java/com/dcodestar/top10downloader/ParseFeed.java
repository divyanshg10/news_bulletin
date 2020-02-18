package com.dcodestar.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseFeed {
    private ArrayList<Feed> feeds;
    private static final String TAG = "ParseFeed";

    public ParseFeed() {
        this.feeds = new ArrayList<>();
    }

    public ArrayList<Feed> getFeeds() {
        return feeds;
    }

    public boolean parse(String xmlData){
        boolean status=true;
        Feed feed=null;
        boolean inItem=false;
        String textvalue="";
        try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType=xpp.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_TAG){
                    if(xpp.getName().equals("item")){
                        inItem=true;
                        feed=new Feed();
                    }
                }else if(eventType==XmlPullParser.TEXT){
                    if(inItem==true){
                        textvalue=xpp.getText();
                    }
                }
                else if(eventType==XmlPullParser.END_TAG){
                    if(xpp.getName().equals("item")){
                        inItem=false;
                        feeds.add(feed);
                    }else if(xpp.getName().equals("title")){
                        if(inItem==true){
                            feed.setTitle(textvalue);
                        }
                    }else if(xpp.getName().equals("description")){
                        if(inItem==true){
                            feed.setDescription(textvalue);
                        }
                    }
                }
                eventType=xpp.next();
            }
            for(Feed f:feeds){
                Log.d(TAG, "****************************************************************");
                Log.d(TAG, f.toString());
            }
        }catch (Exception e){
            status=false;
        }
        return status;
    }
}
