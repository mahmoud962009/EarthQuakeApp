package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.view.MenuItem;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    RecyclerView recyclerView;
    CardAdapter cardAdapter;

    public static List<card> cardList = new ArrayList<>();
    ProgressDialog progressDialog;
    String text;
    String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);


        cardList = new ArrayList<>();
        new AsyncTaskExample().execute(urlSource);
        if (savedInstanceState != null) {
            cardList.clear();
            cardList.addAll(savedInstanceState.getParcelableArrayList("key"));
            cardAdapter = new CardAdapter(MainActivity.this, cardList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(cardAdapter);
        }
    }

    private class AsyncTaskExample extends AsyncTask<String, String, List<card>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected List<card> doInBackground(String... strings) {
            int i = 0;
            card card = null;
            URL url;
            URLConnection urlConnection;
            BufferedReader in = null;

            try {
                url = new URL(strings[0]);
                urlConnection = url.openConnection();
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(urlConnection.getInputStream(), null);
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = parser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tagName.equalsIgnoreCase("item")) {
                                    card = new card();
                                }
                                break;

                            case XmlPullParser.TEXT:
                                text = parser.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if (card != null) {
                                    if (tagName.equalsIgnoreCase("title")) {
                                        card.setTitle(text);
                                    } else if (tagName.equalsIgnoreCase("description")) {
                                        String[] strings1 = text.split(";");
                                        String location = strings1[1].split(":")[1].trim();
                                        String[] latLong = strings1[2].split(":")[1].trim().split(",");
                                        String depth = strings1[3].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        String magnitude = strings1[4].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        card.setDescription(text);
                                        card.setLocation(location);
                                        card.setDepth(Double.parseDouble(depth));
                                        card.setMagnitude(Double.parseDouble(magnitude));
                                        card.setLatitude(Double.parseDouble(latLong[0]));
                                        card.setLongitude(Double.parseDouble(latLong[1]));
                                    } else if (tagName.equalsIgnoreCase("link")) {
                                        card.setLink(text);
                                    } else if (tagName.equalsIgnoreCase("pubDate")) {
                                        card.setPubDate(text);
                                    } else if (tagName.equalsIgnoreCase("category")) {
                                        card.setCategory(text);
                                    } else if (tagName.equalsIgnoreCase("item")) {
                                        i++;
                                        card.setId(i);
                                        cardList.add(card);
                                    }
                                }

                                break;

                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                    Collections.sort(cardList, new Comparator<card>() {
                        public int compare(card obj1, card obj2) {
                            return Double.compare(obj2.getMagnitude(), obj1.getMagnitude());
                        }
                    });
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                in.close();
            } catch (IOException ae) {
                System.out.println(ae);
            }
            return cardList;
        }

        @Override
        protected void onPostExecute(List<card> cardList) {
            super.onPostExecute(cardList);
            if (cardList != null) {
                progressDialog.hide();


                cardAdapter = new CardAdapter(MainActivity.this, cardList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(cardAdapter);
            } else {
                progressDialog.show();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<card>(cardList));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cardList.clear();
        cardList.addAll(savedInstanceState.getParcelableArrayList("key"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.card_filter:
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

}