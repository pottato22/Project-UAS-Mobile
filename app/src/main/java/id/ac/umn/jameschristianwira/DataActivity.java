package id.ac.umn.jameschristianwira;

import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import java.util.List;
import java.util.concurrent.Future;

public class DataActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManagerCompat;

    public ArrayList<Characters> characters;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        this.setTitle("Casts of Game of Thrones");

        characters = new ArrayList<>();

        notificationManagerCompat = NotificationManagerCompat.from(this);

//        new fetchPhoto().execute("http://static.tvmaze.com/uploads/images/medium_portrait/63/158798.jpg");

        new fetchData().execute("cast");

        //viewData();
    }

    private class fetchData extends AsyncTask<String, Void, String>{
        public Bitmap image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            String jsonString = null;

            try{
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

                if(inputStream == null){
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if(stringBuffer.length() == 0){
                    return null;
                }

                jsonString = stringBuffer.toString();

                JSONArray  jsonArray = new JSONArray(jsonString);

                if(true){
                    for(int i=0; i<jsonArray.length(); i++){
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
            }
            catch (MalformedURLException e){
                Log.e("MALFORMED", "MalformedURLException: " + e.getMessage());
            }
            catch (IOException e){
                Log.e("IO", "IOException: " + e.getMessage());
            }
            catch (JSONException e){
                Log.e("JSON", "JSONException: " + e.getMessage());
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(bufferedReader != null){
                    try{
                        bufferedReader.close();
                    }
                    catch (IOException e){
                        Log.e("BUFFEREDIOEXCEPTION", "IOException: " + e.getMessage());
                    }
                }
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("onPostExecute", s);

            viewData();
        }
    }

    private class fetchPhoto extends AsyncTask<String, Bitmap, Bitmap>{
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

    public void viewData(){
        Log.d("viewData", "Start showing data");

        recyclerView = findViewById(R.id.data_recycler_view);
        adapter = new RecyclerViewAdapter(characters, getApplicationContext());
        layoutManager = new LinearLayoutManager(DataActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}