package id.ac.umn.jameschristianwira;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<Characters> characters;
    private Context context;
    private BroadcastReciever reciever;

    public RecyclerViewAdapter(ArrayList<Characters> characters, Context context, BroadcastReciever reciever) {
        this.characters = characters;
        this.context = context;
        this.reciever = reciever;
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

        public RecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvRealName = itemView.findViewById(R.id.list_real_name);
            imageView = itemView.findViewById(R.id.list_photo);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                NotificationManagerCompat notificationManagerCompat;
                //int pos = getAdapterPosition();

                @Override
                public boolean onLongClick(View view) {
                    makeToast("Character will be shown in notification");

                    notificationManagerCompat = NotificationManagerCompat.from(context);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notificationManagerCompat = NotificationManagerCompat.from(context);
                            sendOnChannel(getAdapterPosition());
                        }
                    }, 5000);

                    return true;
                }

                public void sendOnChannel(int pos) {
                    String title = "Hi, I'm " + characters.get(pos).getRealname();
                    String message = "I'm playing as " + characters.get(pos).getCharName() + " in Game of Thrones";

                    Notification notification = new NotificationCompat.Builder(context, Notif.CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_stat_name2)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .build();

                    notificationManagerCompat.notify(2, notification);
                }

            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //makeToast("onClick (short click)");
                    //Move to detail page
                    Intent intent = new Intent(context, DetailActivity.class);

                    intent.putExtra("realname", characters.get(pos).getRealname());
                    intent.putExtra("charname", characters.get(pos).getCharName());
                    intent.putExtra("birthday", characters.get(pos).getBirthday());
                    intent.putExtra("gender", characters.get(pos).getGender());
                    createImageFromBitmap(characters.get(pos).getPhoto());
                    //itemView.getContext().startActivity(intent);
                    ((Activity) context).startActivityForResult(intent, 0);
                }
            });
        }

        public String createImageFromBitmap(Bitmap bitmap) {
            String fileName = "temp";//no .png or .jpg needed
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
                fileName = null;
            }
            return fileName;
        }


    }

    public void makeToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
