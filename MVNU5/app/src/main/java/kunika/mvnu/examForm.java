package kunika.mvnu;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.ResultSet;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class examForm extends Fragment {
String rollno;
EditText name,roll,mobile,mobileParent,email,address1,address2,address3,pin,Course,Sem,gender;
Spinner sem;
ResultSet rs;
    public examForm() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_exam_form, container,false);
        rollno="16cs1006";
        name=root.findViewById(R.id.name);
        roll=root.findViewById(R.id.roll);
        mobile=root.findViewById(R.id.mobile);
        mobileParent=root.findViewById(R.id.mobileParent);
        email=root.findViewById(R.id.email);
        address1=root.findViewById(R.id.address1);
        address2=root.findViewById(R.id.address2);
        address3=root.findViewById(R.id.address3);
        pin=root.findViewById(R.id.pin);
        Course=root.findViewById(R.id.Course);
        Sem=root.findViewById(R.id.Sem);
        gender=root.findViewById(R.id.gender);
        sem=root.findViewById(R.id.sem);
        bindValues();
        return root;
    }
    public void bindValues()
    {
        new GetValues(getActivity(),name,roll,mobile,email,Course,Sem,gender).execute("");
    }
    class GetValues extends AsyncTask<String,Void,String>
    {
        Activity activity;
        EditText rname,rroll,rmobile,remail,rCourse,rSem,rgender;
        public GetValues(Activity activity, EditText name,EditText roll,EditText mobile,EditText email,EditText Course,EditText Sem,EditText gender) {
        this.activity=activity;
        this.rname=name;
        this.rroll=roll;
        this.rmobile=mobile;
        this.remail=email;
        this.rCourse=Course;
        this.rSem=Sem;
        this.rgender=gender;
        }

        @Override
        protected String doInBackground(String... strings) {
            try
            {
              rs=Connectivity.getLogin(rollno);
              if(rs.next())
              {
                  rname.setText(rs.getString("name"));
                  rroll.setText(rs.getString("Roll_No"));
                  rmobile.setText(rs.getString("Mobile_No"));
                  remail.setText(rs.getString("Email"));
                  rCourse.setText(rs.getString("dept_id") );
                  rSem.setText(rs.getString("Sem"));
                  rgender.setText(rs.getString("Gender"));
              }
            }
            catch(Exception e)
            {

            }
            return null;
        }
    }
}
