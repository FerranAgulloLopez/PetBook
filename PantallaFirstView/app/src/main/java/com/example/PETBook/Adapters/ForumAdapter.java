package com.example.PETBook.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.ForumModel;
import com.example.PETBook.Models.Image;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<ForumModel> forumList;
    private ImageView imageProfile;
    private String tipoConexion;
    private TextView numComments;

    public ForumAdapter (Context context, ArrayList<ForumModel> array){
        this.context = context;
        forumList = array;
    }

    @Override
    public int getCount() {
        return forumList.size();
    }

    @Override
    public Object getItem(int position) {
        return forumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.forum_design,null);
        }
        TextView nombreForum = (TextView) convertView.findViewById(R.id.nombreForo);
        //numComments = (TextView) convertView.findViewById(R.id.numberMessagesForum);
        TextView dataCreacionForum = (TextView) convertView.findViewById(R.id.dataCreacioForum);
        TextView creadorForum = (TextView) convertView.findViewById(R.id.nombreCreadorForum);
        TextView descriptionForum = (TextView) convertView.findViewById(R.id.descriptionForum);
        imageProfile = (CircleImageView) convertView.findViewById(R.id.imageView);
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + forumList.get(position).getCreatorMail(), "GET", null);
        nombreForum.setText(forumList.get(position).getTitle());


        //getNumComments(position);


        //System.out.println(forumList.get(position).getComments().get(position).getTamaño());
        String fechaString = forumList.get(position).getCreationDate();
        /*Date dateNew = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            dateNew = format.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        dataCreacionForum.setText(prettyTime.format(dateNew));
*/
        dataCreacionForum.setText(forumList.get(position).getCreationDate());
        creadorForum.setText(forumList.get(position).getCreatorMail());
        descriptionForum.setText(forumList.get(position).getDescription());
        return convertView;
    }


    public void getNumComments(final Integer position){
        tipoConexion = "getNumComments";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/GetForumThread?threadId=" + forumList.get(position).getIDForum(), "GET", null);
    }
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            try {
                int response = output.getInt("code");
                if (response == 200) {
                    // convert string to bitmap
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    Image imagenConversor = Image.getInstance();
                    String image = output.getString("image");
                    Bitmap profileImage = imagenConversor.StringToBitMap(image);
                    imageProfile.setImageBitmap(profileImage);
                    //user.setProfilePicture(profileImage);
                }   else if (output.getInt("code")==404) { // user does not have profile picture
                    imageProfile.setImageResource(R.drawable.troymcclure);
                } else {
                    Toast.makeText(this.context, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(this.context, "The server does not work.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
