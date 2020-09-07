package kunika.mvnu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView error,genderror,course_error,sem_error;
    EditText roll,name,email,mobile,pass,pass2;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid,isValidRoll,isValidGender,isValidCourse,isValidStream;
    Spinner gender,Course,Sem;
    String[] course,gender_Value,sem;
    int j=0,k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name= (EditText) findViewById(R.id.name);
        roll= (EditText) findViewById(R.id.roll);
        Course= (Spinner) findViewById(R.id.Course);
        gender= (Spinner) findViewById(R.id.gender);
        email= (EditText) findViewById(R.id.email);
        mobile= (EditText) findViewById(R.id.mobile);
        pass= (EditText) findViewById(R.id.pass);
        pass2= (EditText) findViewById(R.id.pass2);
        error= (TextView) findViewById(R.id.error);
        Sem=findViewById(R.id.Sem);
        genderror= (TextView) findViewById(R.id.genderror);
        course_error= (TextView) findViewById(R.id.course_error);
        sem_error= (TextView) findViewById(R.id.stream_error);

        gender_Value=getResources().getStringArray(R.array.gender);
        gender.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapt=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,gender_Value);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapt);

        sem=getResources().getStringArray(R.array.sem);
        Sem.setOnItemSelectedListener(this);
        ArrayAdapter<String> at=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,sem);
        at.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sem.setAdapter(at);

        course=getResources().getStringArray(R.array.course);
        Course.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,course);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Course.setAdapter(aa);

        Toast.makeText(Register.this, "Please wait...", Toast.LENGTH_SHORT)
                .show();
    }

    public void Submit(View view) {
        SetValidation();
        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && isValidRoll && isValidCourse && isValidStream && isValidGender) {
            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
            new Regis().execute();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //Validtion Check
    public void SetValidation() {
        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            name.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }
        //Check for valiid roll number
        if (roll.getText().toString().isEmpty()) {
            roll.setError(getResources().getString(R.string.roll_error));
            isValidRoll = false;
        }
        else if(!Pattern.matches("^[0-9]{2,}[a-zA-Z]{2,}[0-9]{4,}$",roll.getText().toString()))
        {
            roll.setError(getResources().getString(R.string.error_invalid_roll));
            isValidRoll=false;
        }
        else
        {
            isValidRoll=true;
        }
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid phone number.
        if (mobile.getText().toString().isEmpty()) {
            mobile.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        }else if(!Patterns.PHONE.matcher(mobile.getText().toString()).matches())
        {
            mobile.setError(getResources().getString(R.string.error_invalid_phone));
            isPhoneValid=false;
        }
        else  {
            isPhoneValid = true;
        }

        // Check for a valid password.
        if (pass.getText().toString().isEmpty()) {
            pass.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (pass.getText().length() < 6) {
            pass.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }else if(!pass.getText().toString().equals(pass2.getText().toString()))
        {
            pass2.setError(getResources().getString(R.string.error_password_match));
            isPasswordValid=false;
        }
        else  {
            isPasswordValid = true;
        }
        //Check for valid gender
        if(gender.getSelectedItem().toString().equals("Select"))
        {
            genderror.setVisibility(View.VISIBLE);
            genderror.setText("Please enter a valid gender");
            isValidGender=false;
        }
        else
        {
            genderror.setVisibility(View.GONE);
            isValidGender=true;
        }
        //Check for valid course
        if(Course.getSelectedItem().toString().equals("Select"))
        {
            course_error.setVisibility(View.VISIBLE);
            course_error.setText("Please enter a valid Course");
            isValidCourse=false;
        }
        else
        {
            course_error.setVisibility(View.GONE);
            isValidCourse=true;
        }
        if(Sem.getSelectedItem().toString().equals("Select"))
        {
            sem_error.setVisibility(View.VISIBLE);
            sem_error.setText("Please enter a valid Semester");
            isValidStream=false;
        }
        else
        {
            course_error.setVisibility(View.GONE);
            isValidStream=true;
        }


    }

    //Database execute
    class Regis extends AsyncTask<String,Void,String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Register.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }
        @Override
        protected String doInBackground(String... strings) {
            String result="",Dept_name="";
            try{
                ResultSet rs = Connectivity.dept_id(Course.getSelectedItem().toString());
                if(rs.next()) {


                    Dept_name = rs.getString("dept_id");

                }
                result = Connectivity.Register(name.getText().toString(), roll.getText().toString(), email.getText().toString(), gender.getSelectedItem().toString(), mobile.getText().toString(), pass.getText().toString(), Dept_name, Sem.getSelectedItem().toString());
            }catch(Exception e)
            {
                e.printStackTrace();
                result=e.toString();
            }
            return result;
        }
        protected void onPostExecute(String result)
        {
            if(result.isEmpty())
            {
                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
            else
            {
                error.setText(result);
            }
        }
    }
    class getValues extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {

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
        }
    }
}
