package kunika.mvnu;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class Notes extends Fragment {

    ArrayList<String> subjects;
    String[] subj;
    ListView l;
    ResultSet rs;
    String roll;
    CardView unit_1,unit_2,unit_3,unit_4,unit_5,unit_6;
    ListView syllabusList;
    int Unit;
    GridLayout gridLayout;
    ArrayList<String> topiclist=new ArrayList<>();
    LinearLayout linearLayout;
    public Notes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        roll="16cs1006";
        View root = inflater.inflate(R.layout.fragment_notes,null);
        l = root.findViewById(R.id.subject);
        syllabusList=root.findViewById(R.id.syllabus);
        linearLayout=root.findViewById(R.id.List);
        gridLayout=root.findViewById(R.id.gridLayout);
        unit_1=root.findViewById(R.id.unit_1);
        unit_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=1;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });
        unit_2=root.findViewById(R.id.unit_2);
        unit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=2;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });
        unit_3=root.findViewById(R.id.unit_3);
        unit_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=3;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });
        unit_4=root.findViewById(R.id.unit_4);
        unit_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=4;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });
        unit_5=root.findViewById(R.id.unit_5);
        unit_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=5;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });
        unit_6=root.findViewById(R.id.unit_6);
        unit_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                l.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                syllabusList.setVisibility(View.VISIBLE);
                Unit=6;
                new getSyllabus(getActivity(),Unit,syllabusList).execute();
            }
        });

        bindListView();
        return root;
    }
    public  void bindListView()
    {
        new GetSubjects(getActivity(),l,linearLayout,gridLayout).execute("");
    }
    class GetSubjects extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView listView;
        LinearLayout linear;
        GridLayout gridLayout;
        public GetSubjects(Activity activity, ListView listView, LinearLayout linearLayout, GridLayout gridLayout) {
            this.listView=listView;
            this.activity= activity;
            this.linear=linearLayout;
            this.gridLayout=gridLayout;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            subjects=new ArrayList<>();
            try
            {
                rs=Connectivity.getCourseName(roll);
                while(rs.next())
                {
                    subjects.add(rs.getString("course_name"));
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
            if(s.isEmpty())
            {
                subj=new String[subjects.size()];
                subj=subjects.toArray(subj);
                MyAdapter aa = new MyAdapter(Notes.this.getActivity(), subj);
                listView.setAdapter(aa);
                justifyListViewHeightBasedOnChildren(listView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        linear.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
            }

        }
        public void justifyListViewHeightBasedOnChildren(ListView listView)
        {
            android.widget.ListAdapter adapter=listView.getAdapter();
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
            TextView subtitle=row.findViewById(R.id.subtitle);
            ImageView image = row.findViewById(R.id.image);
            title.setText(rtitle[position]);
            image.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            //image.setImageResource(rimgs[position]);

            return row;
        }
    }
    class getSyllabus extends AsyncTask<String,Void,String>
    {
        Activity activity;
        int unit;
        ListView syllabusList;
        public getSyllabus(Activity activity, int unit, ListView syllabusList) {
            this.activity=activity;
            this.unit=unit;
            this.syllabusList=syllabusList;
        }

        @Override
        protected String doInBackground(String... strings) {
            ResultSet resultSet;
            String res="";
            try
            {
                resultSet=Connectivity.getSyllabus(roll,unit);
                while(resultSet.next())
                {
                    topiclist.add(resultSet.getString("topic_name"));
                }
            }
            catch (Exception e)
            {

            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.isEmpty())
            {
                String[] strings = new String[topiclist.size()];
                strings=topiclist.toArray(strings);
                MyNewAdapter adapter=new MyNewAdapter(Notes.this.getActivity(),strings );
                syllabusList.setAdapter(adapter);
                justifyListViewHeightBasedOnChildren(syllabusList);
            }
            else
            {
                Toast.makeText(Notes.this.getActivity(), ""+s, Toast.LENGTH_SHORT).show();
            }
        }
        public void justifyListViewHeightBasedOnChildren(ListView listView)
        {
            android.widget.ListAdapter adapter=listView.getAdapter();
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
    class MyNewAdapter extends ArrayAdapter<String> {
        Activity context;
        String rtitle[];
        // int rimgs[];
        //String[] rsubtitle;

        public MyNewAdapter(@NonNull Activity c, String title[]) {
            super(c, R.layout.attend_row, title);
            this.context = c;
            this.rtitle = title;
            //this.rimgs = imgs;
            //this.rsubtitle = subtitle;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l = context.getLayoutInflater();
            View row = l.inflate(R.layout.attend_row, null, true);
            TextView title = row.findViewById(R.id.title);
            //ImageView image = row.findViewById(R.id.image);
            TextView subtitle = row.findViewById(R.id.subtitle);
            title.setText(rtitle[position]);
            //image.setImageResource(rimgs[position]);
            //subtitle.setText(rsubtitle[position]);
            subtitle.setText("View Download");
            subtitle.setTextColor(0xff0000ff);
            return row;
        }
    }
}
