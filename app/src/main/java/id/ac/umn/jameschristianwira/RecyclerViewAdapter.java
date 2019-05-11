package id.ac.umn.jameschristianwira;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<Characters> characters;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Characters> characters, Context context) {
        this.characters = characters;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.character_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
        String realName = characters.get(i).getRealname();
        Bitmap image = characters.get(i).getPhoto();

        recyclerViewHolder.tvRealName.setText(realName);
        recyclerViewHolder.imageView.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return (characters != null ? characters.size() : 0);
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvRealName;
        ImageView imageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRealName = itemView.findViewById(R.id.list_real_name);
            imageView = itemView.findViewById(R.id.list_photo);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                NotificationManagerCompat notificationManagerCompat;

                @Override
                public boolean onLongClick(View view) {
                    makeToast("onLongClick");

                    notificationManagerCompat = NotificationManagerCompat.from(context);

                    sendOnChannel();

                    return true;
                }

                public void sendOnChannel(){
                    int pos = getAdapterPosition();
                    String title = characters.get(pos).getRealname();
                    String message = "Hi, my name is " + title;

                    Notification notification = new NotificationCompat.Builder(context, Notif.CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_one)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .build();

                    notificationManagerCompat.notify(1, notification);
                }

                class TimerStart extends AsyncTask<String, Void, String>{
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                    }

                    @Override
                    protected String doInBackground(String... strings) {

                        int i = 0;
                        while (i<5){
                            try{
                                Thread.sleep(1000);
                                i++;
                            }
                            catch (InterruptedException e){
                                Log.e("TIMERSERVICE", "Timer start - onStartCommand: " + e.getMessage());
                            }
                        }

                        sendOnChannel();

                        return null;
                    }
                }

            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeToast("onClick (short click)");
                    //Move to detail page
                }
            });
        }

        public void makeToast(String text){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
