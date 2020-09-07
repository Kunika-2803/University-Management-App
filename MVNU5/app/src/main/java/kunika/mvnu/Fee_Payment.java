package kunika.mvnu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;

import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;

public class Fee_Payment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee__payment);
        CardForm cardForm=findViewById(R.id.cardform);
        TextView txtview=findViewById(R.id.payment_amount);
        Button btn=findViewById(R.id.btn_pay);
        txtview.setText("1999Rs");
        btn.setText(String.format("Payer %s",txtview.getText()));
        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(Fee_Payment.this, "Name :"+card.getName()+" | Last 4 digits : "+card.getLast4(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}