package id.ac.umn.jameschristianwira;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManagerCompat;

    public ArrayList<Characters> characters;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    BroadcastReciever reciever;

    TextView loadingText, progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        this.setTitle("Casts of Game of Thrones");

        characters = new ArrayList<>();

        notificationManagerCompat = NotificationManagerCompat.from(this);

//        new fetchPhoto().execute("http://static.tvmaze.com/uploads/images/medium_portrait/63/158798.jpg");

        loadingText = findViewById(R.id.data_loadingtext);
        progressText = findViewById(R.id.data_progress);

        if (characters.isEmpty()) {
            new fetchData().execute("cast");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about_me:
                Intent intent = new Intent(DataActivity.this, AboutMe.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class fetchData extends AsyncTask<String, Void, String> {
        public Bitmap image;
        public int length, current;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            String jsonString = null;

            try {
//                String urlString = "http://api.steampowered.com/IEconDOTA2_570/GetGameItems/v1?key=1F18B74411FABB8D9BEA08599C06578F";
//                String urlString = "https://sv443.net/jokeapi/category/Programming?blacklistFlags=nsfw,religious,politics";
                String urlString = "http://api.tvmaze.com/shows/82/cast";
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int lengthOfFile = urlConnection.getContentLength();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }

                jsonString = stringBuffer.toString();

                JSONArray jsonArray = new JSONArray(jsonString);

                length = jsonArray.length();

                if (true) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        current = i;
                        publishProgress();

                        JSONObject theData = jsonArray.getJSONObject(i);

                        JSONObject jsonPerson = new JSONObject(theData.getString("person"));
                        JSONObject jsonChar = new JSONObject(theData.getString("character"));
                        JSONObject jsonPhoto = new JSONObject(jsonPerson.getString("image"));
                        image = new fetchPhoto().doInBackground(jsonPhoto.getString("medium"));
                        //Log.d("GETDATA", "Real name: " + jsonPerson.getString("name"));
                        //Log.d("GETDATA", "Char name: " + jsonChar.getString("name"));

                        Characters character = new Characters(
                                jsonChar.getString("name"),
                                jsonPerson.getString("name"),
                                jsonPerson.getString("birthday"),
                                jsonPerson.getString("gender"),
                                image
                        );

                        characters.add(character);
                        //Log.i("Dota 2 Item", "Real Item Name: " + item.getRealName());
                    }
                }

                //Log.d("FETCHDATA", jsonString);
                Log.d("FETCHDATA", "" + jsonArray.length());
            } catch (MalformedURLException e) {
                Log.e("MALFORMED", "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e("IO", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.e("JSON", "JSONException: " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e("BUFFEREDIOEXCEPTION", "IOException: " + e.getMessage());
                    }
                }
            }

            return jsonString;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            progressText.clearComposingText();
            progressText.setText(current + "/" + length);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("onPostExecute", s);

            ProgressBar progressBar = findViewById(R.id.data_progressbar);

            progressBar.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);

            viewData();
        }
    }

    private class fetchPhoto extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageURL = strings[0];
            Bitmap bimage = null;

            Log.d("TAKE IMAGE", imageURL);

            try {
                InputStream in = new URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    public void viewData() {
        Log.d("viewData", "Start showing data");

        recyclerView = findViewById(R.id.data_recycler_view);
        adapter = new RecyclerViewAdapter(characters, getApplicationContext(), reciever);
        layoutManager = new LinearLayoutManager(DataActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
