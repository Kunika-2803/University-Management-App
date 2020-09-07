package kunika.mvnu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.ResultSet;
import androidx.appcompat.app.AppCompatActivity;

public class Organise extends AppCompatActivity {
    private Spinner ref_teacher;
    private Spinner event_type;
    private Button icon;
    private EditText event_name;
    private EditText event_describe;
    private ImageView display_icon;
    private CheckBox terms;
    private TextView error_teacher,error_type;
    private boolean isValidEvent;
    boolean isValidEventName;
    boolean isValidDescribe;
    boolean isValidterms;
    boolean isValidTeacher;
String Event_Type[],Ref_Teacher[];
static ResultSet rs=null;
static String var="";
private static final int PICK_IMAGE=1;
Uri imageUrl;
    static Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organise);

        Event_Type=getResources().getStringArray(R.array.Event_Type);

        EditText name = findViewById(R.id.name);
        EditText course = findViewById(R.id.course);
        EditText sem = findViewById(R.id.sem);
        event_name=findViewById(R.id.event_name);
        event_describe=findViewById(R.id.event_describe);
        icon=findViewById(R.id.event_icon);
        Button submit = findViewById(R.id.submit);
        ref_teacher=findViewById(R.id.ref_teacher);
        event_type=findViewById(R.id.EventType);
        display_icon=findViewById(R.id.displayicon);
        terms=findViewById(R.id.terms);
        error_teacher=findViewById(R.id.error_teacher);
        error_type=findViewById(R.id.error_event);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);

            }
        });
        Intent getvalue=getIntent();
        var=getvalue.getStringExtra(Event.vari);
        ArrayAdapter<String> at=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,Event_Type);
        at.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_type.setAdapter(at);

        Ref_Teacher=getResources().getStringArray(R.array.Refer_teacher);
        ArrayAdapter<String> ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,Ref_Teacher);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ref_teacher.setAdapter(ad);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
                if(isValidEvent && isValidEventName && isValidDescribe && isValidterms && isValidTeacher)
                {
                    new Organises().execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK)
        {
            imageUrl=data.getData();
            try
            {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imageUrl);
                display_icon.setImageBitmap(bitmap);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void SetValidation()
    {

        if(event_type.getSelectedItem().toString().equals("Select"))
        {
            error_type.setVisibility(View.VISIBLE);
            isValidEvent=false;
        }
        else
        {
            isValidEvent=true;
        }

        if(event_name.getText().toString().isEmpty())
        {
            event_name.setError("Please enter an Event name");
            isValidEventName=false;
        }
        else
        {
            isValidEventName=true;
        }

        if(event_describe.getText().toString().isEmpty())
        {
            event_describe.setError("Please describe event in not more than 150 words");
            isValidDescribe=false;
        }
        else
        {
            isValidDescribe=true;
        }

        if(!terms.isChecked())
        {
            isValidterms=false;
        }
        else {
            isValidterms=true;
        }

        if(ref_teacher.getSelectedItem().toString().equals("Select"))
        {
            error_teacher.setVisibility(View.VISIBLE);
            isValidTeacher=false;
        }
        else
        {
            isValidTeacher=true;
        }
    }
    class Organises extends AsyncTask<String,Void,String>
    {
        String dept_id,sem_id,fac_id,res=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Organise.this, "Loading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                rs=Connectivity.getLogin(var);
                if(rs.next())
                {
                    dept_id=rs.getString("dept_id");
                    sem_id=rs.getString("Sem");
                }
                rs=Connectivity.getTeacher(ref_teacher.getSelectedItem().toString());
                if(rs.next())
                {
                    fac_id=rs.getString("fac_id");
                }
                res=Connectivity.OrganiseEvent(var,dept_id,sem_id,event_type.getSelectedItem().toString(),event_name.getText().toString(),event_describe.getText().toString(),icon.getText().toString(),fac_id);

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.isEmpty())
            {
                Intent email=new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{"crazykrushi44@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT,"Hey this is kunika");
                email.putExtra(Intent.EXTRA_TEXT,"A message from me");

                try {
                    startActivity(Intent.createChooser(email, "Choose an email:"));
                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(Organise.this, "No Email found", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                error_teacher.setText(s);
            }
            super.onPostExecute(s);
        }
    }
}
