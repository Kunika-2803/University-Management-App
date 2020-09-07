package kunika.mvnu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText user,password;
    TextView error;
    CheckBox chkChoice;
    public static final String MYPREFS = "mySharedPreferences";
    public Boolean flag=false,isNameValid,isPassValid;
    public static String vari="kunika.mvnu.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user= (EditText) findViewById(R.id.user);
        password= (EditText) findViewById(R.id.password);
        error= (TextView) findViewById(R.id.error);
        chkChoice=findViewById(R.id.chkChoice);
        Intent intent=getIntent();
        String var=intent.getStringExtra(MainActivity.var);

        this.loadPreferences();
    }

    public void loadPreferences() {
        // Get the stored preferences
        int mode = Activity.MODE_PRIVATE;
        android.content.SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFS, mode);
        // Retrieve the saved values
        user.setText(mySharedPreferences.getString("name", ""));
        password.setText(mySharedPreferences.getString("pass", ""));
    }
    public void Login(View view)
    {

        SetValidation();
        if(isNameValid&&isPassValid) {
            error.setText("");
            new LogIn().execute();
        }
    }
    private class LogIn extends AsyncTask<String,Void,String>
    {
        String name="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Login.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                ResultSet rs = Connectivity.Login(user.getText().toString(),password.getText().toString());
                if(rs.next())
                {
                    flag=true;
                    name=rs.getString("Roll_No");
                }
                else
                {
                    name="Invalid Username or password";
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                name=e.toString();

            }
            return name;
        }
        @Override
        protected void onPostExecute(String result) {
            if(flag)
            {
                Intent inte=new Intent(getApplicationContext(),Student_dash.class);
                inte.putExtra(vari,result);
                startActivity(inte);
            }
            else {
                error.setText(result);
            }
        }
    }
    public void SetValidation()
    {
        if (user.getText().toString().isEmpty()) {
            user.setError(getResources().getString(R.string.user_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.password_error));
            isPassValid = false;
        } else  {
            isPassValid = true;
        }
    }
    public void Register(View view)
    {
        Intent i = new Intent(getApplicationContext(),Register.class);
        startActivity(i);
    }
    protected void savePreferences() {
        // Create or retrieve the shared preference object.
        int mode = Activity.MODE_PRIVATE;
        android.content.SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFS, mode);
        // Retrieve an editor to modify the shared preferences.
        android.content.SharedPreferences.Editor editor = mySharedPreferences.edit();
        // Store data in the shared preferences object.
        editor.putString("name", user.getText().toString());
        editor.putString("pass", password.getText().toString());
        // Commit the changes.
        editor.commit();
    }

    public void chkBox(View view) {
        if(chkChoice.isChecked()) {
            this.savePreferences();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shared_preferences_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void txtForget(View view)
    {
        Intent intent = new Intent(getApplicationContext(),ForgetPassword.class);
        startActivity(intent);
    }
}
