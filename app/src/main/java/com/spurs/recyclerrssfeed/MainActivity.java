package com.spurs.recyclerrssfeed;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Item> items=new ArrayList<>();

    MyAdapter adapter;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recycler);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new MyAdapter(items,this);
        recyclerView.setAdapter(adapter);

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                items.clear();
                readRss();
            }
        });

        //파싱작업
        readRss();
    }

    void readRss(){
        try {
            URL url=new URL("http://rss.hankyung.com/new/news_industry.xml");

            RssFeedTask task=new RssFeedTask();
            task.execute(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Rss XML문서를 네트워크로 읽어오는 작업 스레드..
    class  RssFeedTask extends AsyncTask<URL, Void, String>{

        //Thread의 run()메소드 같은 역할
        @Override
        protected String doInBackground(URL... params) {

            //전달받은 URL객체..
            URL url=params[0];

            try {
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xpp=factory.newPullParser();

                InputStream is=url.openStream();
                xpp.setInput(is,"utf-8");

                int eventType=xpp.next();

                String tag; //태크이름

                Item item=null;

                while (eventType !=XmlPullParser.END_DOCUMENT){

                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            tag=xpp.getName();
                            if(tag.equals("item")){
                                item=new Item();
                            }else if(tag.equals("title")){
                                xpp.next();
                                if(item!=null) item.title=xpp.getText();
                            }else if(tag.equals("link")){
                                xpp.next();
                                if(item!=null) item.link=xpp.getText();
                            }else if(tag.equals("description")){
                                xpp.next();
                                if(item!=null) item.desc=xpp.getText();
                            }else if(tag.equals("image")){
                                xpp.next();
                                if(item!=null) item.imgUrl=xpp.getText();
                            }else if(tag.equals("pubDate")){
                                xpp.next();
                                if(item!=null) item.date=xpp.getText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            tag=xpp.getName();
                            if(tag.equals("item")){
                                items.add(item);
                                //item=null;

                                //리사이클러 아답터에게 데이터가 갱신 되었다는 것을 통지..
                                publishProgress();

                            }
                            break;
                    }
                    eventType=xpp.next();
                }//while문..

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "파싱종료!!";
        }

        //publishProgress()메소드를 호출하면 자동으로 실행되는 메소드
        //UI변경 작업 가능한 메소드...
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }

        //doInBackground()가 종료되면 실행되는 메소드
        //이 메소드안에서는 UI작업 가능!!
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Snackbar.make(recyclerView,s,Snackbar.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }
}
