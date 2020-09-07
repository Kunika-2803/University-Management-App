package kunika.mvnu;


import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Participated extends Fragment {
    static String[] mtitle={"Seminar","College","University","Sports"};
    static int[] image={R.drawable.edit,R.drawable.fees,R.drawable.attendance,R.drawable.event};
    static String[] msubtitle={"Seminar Description","College Description","University Description","Sports Description"};

    public Participated() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_participated,container,false);

        MyAdapter aa=new MyAdapter(this.getActivity(),mtitle,image,msubtitle);
        ListView l=root.findViewById(R.id.ParticipateList);
        l.setAdapter(aa);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {

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
            View rootView = inflater.inflate(R.layout.fragment_participated, container, false);
            return rootView;
        }
    }

}
