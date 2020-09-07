package kunika.mvnu;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
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

import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Events extends Fragment {
    static String[] mtitle={"Sports","College","University","Seminar"};
    static int[] image={R.drawable.edit,R.drawable.fees,R.drawable.attendance,R.drawable.event};
    static String[] msubtitle={"Sports Description","College Description","University Description","Seminar Description"};

    public Events() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_events,container,false);

        MyAdapter aa=new MyAdapter(this.getActivity(),mtitle,image,msubtitle);
        ListView l=root.findViewById(R.id.EventList);
        l.setAdapter(aa);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Events.this.getActivity());
                        alertDialogBuilder.setMessage("Are you sure, You want to participate in Sports event");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(Events.this.getActivity(),"you successfully applied",Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    case 1:
                        AlertDialog.Builder alertDialogBuilde = new AlertDialog.Builder(Events.this.getActivity());
                        alertDialogBuilde.setMessage("Are you sure, You want to participate in College event");
                        alertDialogBuilde.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(Events.this.getActivity(),"you successfully applied",Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuilde.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog alertDialo = alertDialogBuilde.create();
                        alertDialo.show();
                    case 2:
                        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(Events.this.getActivity());
                        alertDialogBuild.setMessage("Are you sure, You want to participate in University event");
                        alertDialogBuild.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(Events.this.getActivity(),"you successfully applied",Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog alertDial = alertDialogBuild.create();
                        alertDial.show();
                    case 3:
                        AlertDialog.Builder alertDialogBuil = new AlertDialog.Builder(Events.this.getActivity());
                        alertDialogBuil.setMessage("Are you sure, You want to participate in Seminar event");
                        alertDialogBuil.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(Events.this.getActivity(),"you successfully applied",Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuil.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog alertDia = alertDialogBuil.create();
                        alertDia.show();
                        default:
                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
    static class MyAdapter extends ArrayAdapter<String>
    {
        Activity context;
        String rtitle[];
        int rimgs[];
        String[] rsubtitle;
        public MyAdapter(@NonNull Activity c, String title[], int imgs[], String[] subtitle) {
            super(c,R.layout.row,title);
            this.context=c;
            this.rtitle=title;
            this.rimgs=imgs;
            this.rsubtitle=subtitle;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l=context.getLayoutInflater();
            View row=l.inflate(R.layout.row,null,true);
            TextView title=row.findViewById(R.id.title);
            ImageView image=row.findViewById(R.id.image);
            TextView subtitle=row.findViewById(R.id.subtitle);
            title.setText(rtitle[position]);
            image.setImageResource(rimgs[position]);
            subtitle.setText(rsubtitle[position]);
            return row;
        }

    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_events, container, false);
            return rootView;
        }
    }
    class GetSubjects extends AsyncTask<String,Void,String>
    {
        Activity activity;
        ListView listView;
        public GetSubjects(Activity activity,ListView listView) {
            this.listView=listView;
            this.activity= activity;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
             /*//   ResultSet rs = Connectivity.getCourseName(roll);
                while(rs.next())
                {
                  //  subjects.add(rs.getString("course_name"));
                }*/
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
                MyAdapter aa = new MyAdapter(Events.this.getActivity(),mtitle, image,msubtitle);
                listView.setAdapter(aa);
            }
            else
            {
                Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
