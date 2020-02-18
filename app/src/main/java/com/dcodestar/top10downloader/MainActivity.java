package com.dcodestar.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloadData=new DownloadData();
        downloadData.execute("https://sports.ndtv.com/rss/cricket");
        Log.d(TAG, "onCreate: done");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        String url;
        switch (id){
            case R.id.cricket:
                url="https://sports.ndtv.com/rss/cricket";
                break;

            case R.id.all:
                url="https://sports.ndtv.com/rss/all";
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        new DownloadData().execute(url);
        return true;
    }

    private class DownloadData extends AsyncTask<String,Void,String>{
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is "+s);
            ParseFeed parseFeed=new ParseFeed();
            parseFeed.parse(s);
//            ArrayAdapter<Feed> arrayAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.list_item, parseFeed.getFeeds());
//            listView.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter=new FeedAdapter(MainActivity.this,R.layout.list_record,parseFeed.getFeeds());
            listView.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with "+strings[0]);
            String rssFeed=downloadXML(strings[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath){
            StringBuilder xmlResult=new StringBuilder();

            try{
                URL url=new URL(urlPath);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                int response=connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was "+response);
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer=new char[500];
                while(true){
                    charsRead=reader.read(inputBuffer);
                    if(charsRead<0){
                        break;
                    }
                    if(charsRead>0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL "+e.getMessage() );
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IO Exception reading data: "+e.getMessage());
            }

            return null;
        }
    }
}
