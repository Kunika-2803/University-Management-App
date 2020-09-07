package kunika.mvnu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Barrier;

public class Student_dash extends AppCompatActivity {
TextView name;
private static final int PICK_IMAGE=1;
ImageView display_icon;
    Uri imageUrl;
CardView attendance,study,fees,library,gradesheet;
    public static String vari="kunika.mvnu.MESSAGE";
    String roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash);

        Intent intent=getIntent();
        roll=intent.getStringExtra(Login.vari);
        display_icon=findViewById(R.id.imageView);
        new UploadImageTask().execute();
        attendance=findViewById(R.id.attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Attendance.class);
                i.putExtra(vari,roll);
                startActivity(i);
            }
        });
        study=findViewById(R.id.Study);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Study_Material.class);
                i.putExtra(vari,roll);
                startActivity(i);
            }
        });
        fees=findViewById(R.id.feePayment);
        fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Fee_Payment.class);
                startActivity(i);
            }
        });
        library=findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Library.class);
                i.putExtra(vari,roll);
                startActivity(i);
            }
        });
        gradesheet=findViewById(R.id.gradesheet);
        gradesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Examination.class);
                startActivity(i);
            }
        });
        name= (TextView) findViewById(R.id.name);
        name.setText(roll);
    }
    public void Edit(View view)
    {
        Intent i=new Intent(getApplicationContext(),EditProfile.class);
        i.putExtra(vari,roll);
        startActivity(i);
    }
    public void Event(View view)
    {
        Intent i=new Intent(getApplicationContext(),Event.class);

        i.putExtra(vari,roll);
        startActivity(i);
    }

    ResultSet rs;
    class UploadImageTask extends AsyncTask<String,Void,String>
    {
        String res="";
        @Override
        protected String doInBackground(String... strings) {
            try
            {
                rs=Connectivity.getImage(roll);
                if(rs.next())
                {
                    Blob blob = rs.getBlob("Image");
                    byte barr[]=blob.getBytes(1, (int) blob.length());
                    Bitmap bitmap=BitmapFactory.decodeByteArray(barr,0 ,barr.length);
                    display_icon.setImageBitmap(bitmap);
                }
            }
            catch (Exception e)
            {

            }
            return res;
        }
    }
}
