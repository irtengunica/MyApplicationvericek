package com.example.okul.myapplicationvericek;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    private List<String> titleList;
    private List<String> linkList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // rss baþlýklarýný tutacak list
        titleList = new ArrayList<String>();
        // rss linklerini tutacak list
        linkList = new ArrayList<String>();

        try {
            // rss url'imiz
            URL url = new URL("http://rss.feedsportal.com/c/32892/f/530178/index.rss");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openConnection().getInputStream(), "UTF_8");
            boolean insideItem = false;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (parser.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            titleList.add(parser.nextText());
                    } else if (parser.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            linkList.add(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG &&
                        parser.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                }
                eventType = parser.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titleList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // listteki konu baþlýðýna týklanýnca bizi siteye yönlendirsin
        Uri uri = Uri.parse(linkList.get(position));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
