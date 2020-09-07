package kunika.mvnu;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Date_Sheet extends Fragment {
ListView listDate,listDateSheet;
    ArrayList<Date> dateSheet=new ArrayList<>();
    ArrayList<String> subject_name=new ArrayList<>();
    ArrayList<Time> start_time=new ArrayList<>();
    ArrayList<Time> end_time=new ArrayList<>();
    ArrayList<String> msession_name=new ArrayList<>();
    Date[] mdate;
    String[] msession;
    String[] subject;
    Time[] startTime;
    Time[] endTime;
    String roll;
    ResultSet rs;
    ArrayList<String> dept=new ArrayList<>();
    ArrayList<String> Semes=new ArrayList<>();
    public Date_Sheet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_date__sheet, container, false);
        listDate=root.findViewById(R.id.listDate);
        listDateSheet=root.findViewById(R.id.listDateSheet);
        roll="16cs1006";
        new GetDateSheet(getActivity(),listDate).execute("");

        listDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listDateSheet.setVisibility(View.VISIBLE);
                listDate.setVisibility(View.GONE);
                bindDate_Sheet();
                listDateSheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        });
    //    justifyListViewHeightBasedOnChildren(listDate);
       // justifyListViewHeightBasedOnChildren(listDateSheet);
        return root;
    }
    public void bindDate_Sheet()
    {
        new Date_SheetTask(getActivity(),listDateSheet).execute("");
    }

    class GetDateSheet extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView rlistDate;
        public GetDateSheet(Activity activity, ListView listDate) {
            this.activity=activity;
            this.rlistDate=listDate;
        }

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                rs=Connectivity.getDepartment(roll);
                if(rs.next())
                {
                    dept.add(rs.getString("dept_name"));
                    Semes.add(rs.getString("Sem"));
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] s1 = new String[dept.size()];
            s1=dept.toArray(s1);
            String[] s2 = new String[Semes.size()];
            s2=Semes.toArray(s2);
            MyAdapter adapter=new MyAdapter(Date_Sheet.this.getActivity(), s1, s2);
            rlistDate.setAdapter(adapter);
        }
    }
    class MyAdapter extends ArrayAdapter<String> {
        Activity context;
        String rtitle[];
        String[] rsubtitle;
        public MyAdapter(@NonNull Activity c, String title[], String[] subtitle) {
            super(c, R.layout.row, title);
            this.context = c;
            this.rtitle = title;
            this.rsubtitle = subtitle;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l = context.getLayoutInflater();
            View row = l.inflate(R.layout.row, null, true);
            TextView title = row.findViewById(R.id.title);
            TextView subtitle=row.findViewById(R.id.subtitle);
            title.setText(rtitle[position]);
            subtitle.setText(rsubtitle[position]);
            return row;
        }
    }
    class Date_SheetAdapter extends ArrayAdapter<String>
    {
        Activity activity;
        Date[] rdate;
        String[] rsubject_name;
        String[] rsession_name;
        Time[] rstart;
        Time[] rend;

        public Date_SheetAdapter(@NotNull Activity context, String[] subject_name, Date[] date, String[] session_name, Time[] start, Time[] end ) {
            super(context, R.layout.time_table_row,subject_name);
            this.activity=context;
            this.rend=end;
            this.rstart=start;
            this.rsubject_name=subject_name;
            this.rdate=date;
            this.rsession_name=session_name;
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
            periods.setText(rdate[position].toString());
            subject_name.setText(rsubject_name[position]);
            faculty_name.setText(rsession_name[position]);
            start_time.setText(rstart[position].toString());
            end_time.setText(rend[position].toString());
            return row;
        }
    }
    class Date_SheetTask extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView details;
        public Date_SheetTask(Activity activity, ListView detail) {
            this.details=detail;
            this.activity=activity;
        }
        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                rs=Connectivity.getDateSheet(roll);
                while(rs.next())
                {
                    dateSheet.add(rs.getDate("date"));
                    subject_name.add(rs.getString("course_name"));
                    start_time.add(rs.getTime("start_time"));
                    end_time.add(rs.getTime("end_time"));
                    msession_name.add(rs.getString("Session"));
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
                mdate=new Date[dateSheet.size()];
                mdate= dateSheet.toArray(mdate);
                subject=new String[subject_name.size()];
                subject=subject_name.toArray(subject);
                startTime=new Time[start_time.size()];
                startTime=start_time.toArray(startTime);
                endTime=new Time[end_time.size()];
                endTime=end_time.toArray(endTime);
                msession=new String[msession_name.size()];
                msession=msession_name.toArray(msession);
                Date_SheetAdapter time_tableAdapter = new Date_SheetAdapter(Date_Sheet.this.getActivity(), subject,mdate,msession, startTime,endTime );
                details.setAdapter(time_tableAdapter);
                justifyListViewHeightBasedOnChildren(details);
            }
            else
            {
                Toast.makeText(getContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
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
}
