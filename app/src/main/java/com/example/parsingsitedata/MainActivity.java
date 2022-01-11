package com.example.parsingsitedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItem = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItem, this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();

    }

    private class Content extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{


                String url = "https://www.yellowpages.com/honolulu-hi/play-bar-waikiki";
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("div.result");
                int size = data.size();


                for (int i = 0; i < size; i++)
                {

                    //put the image class in here
                    String imgURL = data.select("div.media-thumbnail")
                            .select("img")
                            .eq(i)
                            .attr("src");

                    String title = data.select("a.business-name")
                            //span is the class you need
                            .select("span")
                            .eq(i)
                            .text();

                    new ParseItem (imgURL, title);

                    parseItem.add(new ParseItem(imgURL, title));
                    Log.d("items", "img: " +imgURL + " . title: " + title);
                }

                System.out.println("+++++++++++++++++++++++++++++THIS IS ACTUALLY RUNNING ");

                /*
                String url = "https://www.cinemaqatar.com";
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("span.thumbnail");
                int size = data.size();


                for (int i = 0; i < size; i++)
                {

                    //put the image class in here
                    String imgURL = data.select("span.thumbnail")
                            .select("img")
                            .eq(i)
                            .attr("src");

                    String title = data.select("h4.gridminfotitle")
                            //span is the class you need
                    .select("span")
                            .eq(i)
                            .text();

                    new ParseItem (imgURL, title);

                    parseItem.add(new ParseItem(imgURL, title));
                    Log.d("items", "img: " +imgURL + " . title: " + title);
                }


                 */

            }
            catch(Exception e){
                System.out.println("THERE IS AN ERROR");
            }
            return null;
        }
    }


}