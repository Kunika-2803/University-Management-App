package kunika.mvnu;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Time_Table extends Fragment {
    static String[] mtitle = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday"};
    ArrayList<Integer> period_no=new ArrayList<>();
    ArrayList<String> period_name=new ArrayList<>();
    ArrayList<Time> start_time=new ArrayList<>();
    ArrayList<Time> end_time=new ArrayList<>();
    ArrayList<String> teacher_name=new ArrayList<>();
    Integer[] periodNo;
    String[] teacher;
    String[] subject;
    Time[] startTime;
    Time[] endTime;
    String day="";
    ResultSet rs = null;
    ListView detail;
    public Time_Table() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_time__table, container, false);
        MyAdapter aa = new MyAdapter(this.getActivity(), mtitle);
        final ListView l = root.findViewById(R.id.tablelist);
        detail=root.findViewById(R.id.detaillist);
        l.setAdapter(aa);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                day = mtitle[position];
                detail.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                bindListView();
                detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        });
        return root;
    }
    public  void bindListView()
    {
        new GetTimeTable(getActivity(),detail).execute("");
    }

    class MyAdapter extends ArrayAdapter<String> {
        Activity context;
        String rtitle[];
       // int rimgs[];
        //String[] rsubtitle;

        public MyAdapter(@NonNull Activity c, String title[]) {
            super(c, R.layout.row, title);
            this.context = c;
            this.rtitle = title;
            //this.rimgs = imgs;
            //this.rsubtitle = subtitle;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l = context.getLayoutInflater();
            View row = l.inflate(R.layout.row, null, true);
            TextView title = row.findViewById(R.id.title);
            ImageView image = row.findViewById(R.id.image);
            TextView subtitle = row.findViewById(R.id.subtitle);
            title.setText(rtitle[position]);
            image.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            //image.setImageResource(rimgs[position]);
            //subtitle.setText(rsubtitle[position]);
            return row;
        }
    }
    class GetTimeTable extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView details;
        public GetTimeTable(Activity activity, ListView detail) {
            this.details=detail;
            this.activity=activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                rs=Connectivity.getTimeTable(day);
                while(rs.next())
                {
                    period_no.add(rs.getInt("period_no"));
                    period_name.add(rs.getString("course_name"));
                    start_time.add(rs.getTime("start_time"));
                    end_time.add(rs.getTime("end_time"));
                    teacher_name.add(rs.getString("name"));
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                res=e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.isEmpty())
            {
                periodNo=new Integer[period_no.size()];
                periodNo= period_no.toArray(periodNo);
                subject=new String[period_name.size()];
                subject=period_name.toArray(subject);
                startTime=new Time[start_time.size()];
                startTime=start_time.toArray(startTime);
                endTime=new Time[end_time.size()];
                endTime=end_time.toArray(endTime);
                teacher=new String[teacher_name.size()];
                teacher=teacher_name.toArray(teacher);
                Time_TableAdapter time_tableAdapter = new Time_TableAdapter(Time_Table.this.getActivity(), subject,periodNo,teacher , startTime,endTime );
                details.setAdapter(time_tableAdapter);
            }
            else
            {
                Toast.makeText(getContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class Time_TableAdapter extends ArrayAdapter<String>
    {
        Activity activity;
        Integer[] rperiod;
        String[] rsubject_name;
        String[] rfaculty_name;
        Time[] rstart;
        Time[] rend;

        public Time_TableAdapter(@NotNull Activity context, String[] subject_name, Integer[] period, String[] faculty_name, Time[] start, Time[] end ) {
            super(context, R.layout.time_table_row,subject_name);
            this.activity=context;
            this.rend=end;
            this.rstart=start;
            this.rsubject_name=subject_name;
            this.rperiod=period;
            this.rfaculty_name=faculty_name;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View row=layoutInflater.inflate(R.layout.time_table_row, null,true);
            TextView periods=row.findViewById(R.id.period);
            TextView subject_name=row.findViewById(R.id.period_name);
            TextView faculty_name=row.findViewById(R.id.teacher_name);
            TextView start_time=row.findViewById(R.id.start_time);
            TextView end_time=row.findViewById(R.id.end_time);
            periods.setText(Integer.toString(rperiod[position]));
            subject_name.setText(rsubject_name[position]);
            faculty_name.setText(rfaculty_name[position]);
            start_time.setText(rstart[position].toString());
            end_time.setText(rend[position].toString());
            return row;
        }
    }
}
