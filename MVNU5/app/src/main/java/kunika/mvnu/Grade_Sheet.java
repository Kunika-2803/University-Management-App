package kunika.mvnu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Grade_Sheet extends Fragment {

    ListView l;
    ArrayList<String> subject=new ArrayList<>();
    ArrayList<String> grades=new ArrayList<>();
    String[] msubjects,mgrades;
    ResultSet rs;
    String roll;
    public Grade_Sheet() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        roll="16cs1006";
        View root=inflater.inflate(R.layout.fragment_grade__sheet, null);
        l=root.findViewById(R.id.listResult);
        bindResult();

        return root;
    }
    public void bindResult()
    {
        new Grade_SheetTask(getActivity(),l).execute("");
    }
    class MyAdapter extends ArrayAdapter<String>
    {
        Activity context;
        String[] rtitle;
        String[] rsubtitle;
        public MyAdapter(@NonNull Activity c, String[] title, String[] subtitle) {
            super(c,R.layout.attend_row,title);
            this.context=c;
            this.rtitle=title;
            this.rsubtitle=subtitle;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l= context.getLayoutInflater();
            View row=l.inflate(R.layout.attend_row,null,true);
            TextView title=row.findViewById(R.id.title);
            TextView subtitle=row.findViewById(R.id.subtitle);
            title.setText(rtitle[position]);
            subtitle.setText(rsubtitle[position]);
            return row;
        }

    }
    class Grade_SheetTask extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView listView;
        public Grade_SheetTask(Activity activity, ListView listview) {
            this.listView=listview;
            this.activity=activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                rs=Connectivity.getGradeSheet(roll);
                while(rs.next())
                {
                    subject.add(rs.getString("course_name"));
                    grades.add(rs.getString("grades"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                res=e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.isEmpty()) {
                msubjects = new String[subject.size()];
                msubjects = subject.toArray(msubjects);
                mgrades = new String[grades.size()];
                mgrades = grades.toArray(mgrades);
                MyAdapter adapter=new MyAdapter(Grade_Sheet.this.getActivity(), msubjects, mgrades);
                listView.setAdapter(adapter);
                justifyListViewHeightBasedOnChildren(listView);
            }
            else
            {
                Toast.makeText(Grade_Sheet.this.getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
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
