package kunika.mvnu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static String var="kunika.mvnu.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void Student(View view)
    {
        Intent intent=new Intent(getApplicationContext(),Login.class);
        intent.putExtra(var,"Student");
        startActivity(intent);
    }
    public void Professor(View view)
    {
        Intent intent=new Intent(getApplicationContext(),Login.class);

        intent.putExtra(var,"Professor");
        startActivity(intent);
    }

}
