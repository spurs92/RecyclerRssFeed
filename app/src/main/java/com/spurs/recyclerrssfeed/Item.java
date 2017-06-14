package com.spurs.recyclerrssfeed;

/**
 * Created by alfo06-11 on 2017-06-14.
 */

public class Item {

    String title;
    String link;
    String desc;
    String date;
    String imgUrl;

    public Item(){

    }

    public Item(String title, String link, String desc, String date, String imgUrl) {
        this.title = title;
        this.link = link;
        this.desc = desc;
        this.date = date;
        this.imgUrl = imgUrl;
    }
}
