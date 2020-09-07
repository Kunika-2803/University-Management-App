package kunika.mvnu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;

import java.sql.ResultSet;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPassword extends AppCompatActivity {
EditText roll;
EditText email;
TextView error;
LinearLayout firstStep,secondStep;
Button submit;
EditText pass,password;
boolean isValid;
    boolean isPasswordValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        roll=findViewById(R.id.roll);
        email=findViewById(R.id.email);
        error=findViewById(R.id.error);
        pass=findViewById(R.id.pass);
        password=findViewById(R.id.password);
        firstStep=findViewById(R.id.firstStep);
        secondStep=findViewById(R.id.secondStep);
        submit=findViewById(R.id.Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
                if(isPasswordValid) {
                    new UpdatePassword().execute();
                }
            }
        });
    }
    public void Next(View view)
    {
        new verify().execute();
        if(isValid)
        {
            firstStep.setVisibility(View.GONE);
            secondStep.setVisibility(View.VISIBLE);
        }
    }
    class verify extends AsyncTask<String,Void,String>
    {
        ResultSet rs;
        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                rs=Connectivity.Verify(roll.getText().toString(), email.getText().toString());
                if(rs.next())
                {
                }
                else
                {
                    res="You have not entered correct details";
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
                isValid=true;
            }
            else
            {
                error.setText("You have not entered correct details");
                isValid=false;
            }
        }
    }
    class UpdatePassword extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                res=Connectivity.SetNewPassword(roll.getText().toString(),pass.getText().toString() );
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
                Toast.makeText(ForgetPassword.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ForgetPassword.this, "Not changed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void SetValidation()
    {

        if (pass.getText().toString().isEmpty()) {
            pass.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (pass.getText().length() < 6) {
            pass.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }else if(!pass.getText().toString().equals(password.getText().toString()))
        {
            password.setError(getResources().getString(R.string.error_password_match));
            isPasswordValid=false;
        }
        else  {
            isPasswordValid = true;
        }
    }
}
