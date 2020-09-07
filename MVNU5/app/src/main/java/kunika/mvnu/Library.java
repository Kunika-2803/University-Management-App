package kunika.mvnu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Library extends AppCompatActivity {
ListView available_books,books;
Button show_books,reissue_book;
GridLayout layout;
ArrayList<String> issued_books;
ArrayList<Date> issued_date;
ArrayList<Date> return_date;
ArrayList<String> all_books,mybooks;
ArrayList<String> all_author;
ArrayList<Integer> copies;
ResultSet rs;
String roll;
String[] book,author;
Integer[] copy;
SearchView search;
SearchableAdapter search_books;
MyNewAdapter adapter;
IntentResult result;
    Uri imageUrl;
    ImageView display_icon;
    private static final int PICK_IMAGE=1;
    ImageButton edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        display_icon = findViewById(R.id.imageView);
        available_books = findViewById(R.id.available_Books);
        books = findViewById(R.id.books);
        search = findViewById(R.id.search);
        show_books = findViewById(R.id.show_books);
        reissue_book = findViewById(R.id.reissue_book);
        layout = findViewById(R.id.layout);
        Intent i = getIntent();
        roll = i.getStringExtra(Student_dash.vari);

        Lib a = new Lib();
        a.execute();
        show_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_books.getText().equals("Show Books")) {
                    layout.setVisibility(View.VISIBLE);
                    show_books.setText("Hide Books");
                } else {
                    layout.setVisibility(View.GONE);
                    show_books.setText("Show Books");
                }
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (all_books.contains(query)) {
                    search_books = new SearchableAdapter(getApplicationContext(), all_books);
                    search_books.getFilter().filter(query);
                    available_books.setAdapter(search_books);
                } else if (all_author.contains(query)) {
                    adapter = new MyNewAdapter(getApplicationContext(), author, book);
                    available_books.setAdapter(adapter);
                    adapter.getFilter().filter(query);
                } else {

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                available_books.setVisibility(View.GONE);
                if (all_books.contains(newText)) {
                    available_books.setVisibility(View.VISIBLE);
                    search_books = new SearchableAdapter(getApplicationContext(), all_author);
                    available_books.setAdapter(search_books);
                    search_books.getFilter().filter(newText);
                } else if (all_author.contains(newText)) {
                    available_books.setVisibility(View.VISIBLE);
                    adapter = new MyNewAdapter(getApplicationContext(), author, book);
                    available_books.setAdapter(adapter);
                    adapter.getFilter().filter(newText);
                } else {

                }
                return false;
            }
        });
        reissue_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanow();
            }
        });

    }
    class Lib extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String response="";
            ResultSet rs2;
            issued_books=new ArrayList<>();
            issued_date=new ArrayList<>();
            return_date=new ArrayList<>();
            all_books=new ArrayList<>();
            mybooks=new ArrayList<>();
            all_author=new ArrayList<>();
            copies=new ArrayList<>();
            try
            {
                rs=Connectivity.getBooks(roll);
                while (rs.next())
                {
                    issued_books.add(rs.getString("book_title"));
                    issued_date.add(rs.getDate("issue_date"));
                    return_date.add(rs.getDate("Return_date"));
                    mybooks.add(rs.getString("book_id"));
                }
                rs2=Connectivity.getAllBooks();
                while(rs2.next())
                {
                    String temp=rs2.getString("book_title");
                    String tmp=rs2.getString("book_author");
                    int temp1=rs2.getInt("no_of_Copies");
                    all_books.add(temp);
                    all_author.add(tmp);
                    copies.add(temp1);
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
                String m[] = new String[issued_books.size()];
                m = issued_books.toArray(m);
                Date n[]=new Date[issued_date.size()];
                n=issued_date.toArray(n);
                Date k[]=new Date[return_date.size()];
                k=return_date.toArray(k);
                MyAdapter at=new MyAdapter(getApplicationContext(),m,k,n);
                books.setAdapter(at);
                book=new String[all_books.size()];
                book=all_books.toArray(book);
                author=new String[all_author.size()];
                author=all_author.toArray(author);
                copy=new Integer[copies.size()];
                copy=copies.toArray(copy);
            }
            else
            {
                Toast.makeText(Library.this, "Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }
    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        String[] rtitle;
        Date[] rsubtitle;
        Date[] rsutitle;
        public MyAdapter(@NonNull Context c, String[] title, Date[] subtitle,Date[] sutitle) {
            super(c,R.layout.row,title);
            this.context=c;
            this.rtitle=title;
            this.rsubtitle=subtitle;
            this.rsutitle=sutitle;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater l= (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=l.inflate(R.layout.lib_row,null,true);
            TextView title=row.findViewById(R.id.title);
            TextView subtitle=row.findViewById(R.id.subtitle);
            TextView sutitle=row.findViewById(R.id.sutitle);
            title.setText(rtitle[position]);
            subtitle.setText(rsubtitle[position]+"");
            sutitle.setText(rsutitle[position]+"");
            return row;
        }

    }

    class MyNewAdapter extends ArrayAdapter<String>
    {
        Context context;
        String[] rtitle;
        String[] rsubtitle;
        public MyNewAdapter(@NonNull Context c, String[] title,String[] subtitle) {
            super(c,R.layout.attend_row,title);
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
    public class SearchableAdapter extends BaseAdapter implements Filterable {

        private List<String> originalData = null;
        private List<String> filteredData = null;
        private LayoutInflater mInflater;
        private ItemFilter mFilter = new ItemFilter();

        public SearchableAdapter(Context context, List<String> data) {
            this.filteredData = data;
            this.originalData = data;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return filteredData.size();
        }

        public Object getItem(int position) {
            return filteredData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unnecessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.attend_row, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.title);
                //holder.text=convertView.findViewById(R.id.subtitle);
                // Bind the data efficiently with the holder.

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // If weren't re-ordering this you could rely on what you set last time
            holder.text.setText(filteredData.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView text;
        }

        public Filter getFilter() {
            return mFilter;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<String> list = originalData;

                int count = list.size();
                final ArrayList<String> nlist = new ArrayList<String>(count);

                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"Result Not Found", Toast.LENGTH_SHORT).show();
            }
            else{
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
                alertdialogbuilder.setMessage(result.getContents()+"\n\nScan Again ?");
                alertdialogbuilder.setTitle("Result Scanned");
                alertdialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanow();
                    }
                });
                alertdialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.show();
                if(mybooks.contains(result.getContents()));
                {

                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanow(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Book ID");
        integrator.initiateScan();
    }
}
