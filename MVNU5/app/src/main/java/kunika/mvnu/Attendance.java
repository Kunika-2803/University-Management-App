package kunika.mvnu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Attendance extends AppCompatActivity {
ListView list,percentList;
String roll;
ResultSet rs,rs2;
ArrayList<String> subjectList;
ArrayList<String> percent;
TextView error,roll_no;
Uri imageUrl;
ImageView display_icon;

    private static final int PICK_IMAGE=1;
    ImageButton edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        list=findViewById(R.id.subject);
        percentList=findViewById(R.id.percent);
        error=findViewById(R.id.error);
        Intent i=getIntent();
        roll=i.getStringExtra(Student_dash.vari);
        roll_no=findViewById(R.id.roll);
        Attend a=new Attend();
        a.execute();


    }

    class Attend extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            subjectList=new ArrayList<>();
            percent=new ArrayList<>();
            try
            {

                rs=Connectivity.getAttendance(roll);
                while(rs.next())
                {
                    String temp1=rs.getString("present");
                    String tmp=rs.getString("course_name");

                    subjectList.add(tmp);
                    percent.add(temp1);
                }
                rs2=Connectivity.getAttendancePercent(roll);
                if(rs.next())
                {
                    roll_no.setText(rs.getString("AVG(present)"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                response=e.toString();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.isEmpty()) {
                String m[] = new String[subjectList.size()];
                m = subjectList.toArray(m);
                String n[]=new String[percent.size()];
                n=percent.toArray(n);
                MyAdapter at=new MyAdapter(getApplicationContext(),m,n);
                list.setAdapter(at);
                justifyListViewHeightBasedOnChildren(list);
            }
            else
            {

            }
            /*MyAdapter aa=new MyAdapter(getApplicationContext(),m);
            list.setAdapter(aa);
            String n[]=new String[percent.size()];
            n=subjectList.toArray(n);
            MyAdapter at=new MyAdapter(getApplicationContext(),n);
            percentList.setAdapter(at);*/
        }
    }
    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        String[] rtitle;
        String[] rsubtitle;
        public MyAdapter(@NonNull Context c, String[] title, String[] subtitle) {
            super(c,R.layout.row,title);
            this.context=c;
            this.rtitle=title;
            this.rsubtitle=subtitle;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l= (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=l.inflate(R.layout.attend_row,null,true);
            TextView title=row.findViewById(R.id.title);
            TextView subtitle=row.findViewById(R.id.subtitle);

            title.setText(rtitle[position]);
            subtitle.setText(rsubtitle[position]);
            return row;
        }

    }
    public void justifyListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter adapter=listView.getAdapter();
        if(adapter==null)
        {
            return;
        }
        ViewGroup viewGroup = listView;
        int totalheight=0;
        for(int i=0;i<adapter.getCount();i++)
        {
            View listItem= adapter.getView(i,null , viewGroup);
            listItem.measure(0,0 );
            totalheight+=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params=listView.getLayoutParams();
        params.height=totalheight+(listView.getDividerHeight()*(adapter.getCount()-1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
