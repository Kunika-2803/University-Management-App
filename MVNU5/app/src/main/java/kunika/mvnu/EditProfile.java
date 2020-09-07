package kunika.mvnu;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class EditProfile extends AppCompatActivity {
    CardView personal_info,general,guardian_details,contact_details;
    LinearLayout personal,general_info,contact_info,guardian_info;
    GridLayout gridLayout;
    EditText myname,roll,Course,Sem,gender,Start_Session,End_Session;
    EditText pass1,pass2,pass3;
    Button edit_details;
    Button change_password;
    EditText mobile,email;
    Button verify_phone,verify_email;
    String roll_no;
    ResultSet rs;
    MySmsBroadCastReceiver mySMSBroadCastReceiver;
    GoogleApiClient mGoogleApiClient;
    private int RESOLVE_HINT = 2;
    private static final int PICK_IMAGE=1;
    ImageView edit;
    boolean isPasswordValid;
    TextView inputOtp;
    Uri imageUrl;
    ImageView display_icon;
    String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        roll_no=intent.getStringExtra(Student_dash.vari);
        display_icon=findViewById(R.id.imageView);
        personal=findViewById(R.id.personal);
        gridLayout=findViewById(R.id.gridLayout);

        myname=findViewById(R.id.myname);
        roll=findViewById(R.id.roll);
        roll.setText(roll_no);
        Course=findViewById(R.id.Course);
        Sem=findViewById(R.id.Sem);
        gender=findViewById(R.id.gender);
        Start_Session=findViewById(R.id.Start_Session);
        End_Session=findViewById(R.id.End_Session);
        edit_details=findViewById(R.id.edit_details);

        personal_info=findViewById(R.id.personal_info);
        personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);
                new My_PersonalTask().execute();
                edit_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdateTask().execute();
                    }
                });
            }
        });

        general_info=findViewById(R.id.general_info);
        pass1=findViewById(R.id.pass1);
        pass2=findViewById(R.id.pass2);
        pass3=findViewById(R.id.pass3);
        change_password=findViewById(R.id.change_password);

        general=findViewById(R.id.general);
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                general_info.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);
                change_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetValidation();
                        if(isPasswordValid) {
                            new UpdatePassword().execute();
                        }
                    }
                });
            }
        });


        guardian_details=findViewById(R.id.guardian_details);
        guardian_info=findViewById(R.id.guardian_info);
        guardian_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardian_info.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);

            }
        });


        contact_info=findViewById(R.id.contact_info);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        verify_email=findViewById(R.id.verify_email);
        verify_phone=findViewById(R.id.verify_phone);
        inputOtp=findViewById(R.id.inputOtp);
        contact_details=findViewById(R.id.contact_details);
        contact_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_info.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);
                verify_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
                        alertDialogBuilder.setMessage("Are you sure, You want to continue with "+email.getText().toString()+" email");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(EditProfile.this,"Verified Otp",Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                email.setEnabled(true);
                            }
                        });


                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
                verify_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
                        alertDialogBuilder.setMessage("Are you sure, You want to continue with "+mobile.getText().toString()+" number");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(EditProfile.this,"Verified Otp",Toast.LENGTH_LONG).show();
                                            }
                                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mobile.setEnabled(true);
                                    }
                                });


                                AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });

            }
        });
      /*  mySMSBroadCastReceiver = new MySmsBroadCastReceiver();

        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(EditProfile.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this,this )
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mySMSBroadCastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mySMSBroadCastReceiver, intentFilter);
        // get mobile number from phone
        getHintPhoneNumber();
        //start SMS listner
        smsListener();*/
      edit=findViewById(R.id.edit);
      edit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent gallery=new Intent();
              gallery.setType("image/*");
              gallery.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
          }
      });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK)
        {
            imageUrl=data.getData();
            try
            {
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(imageUrl,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imageUrl);
                display_icon.setImageBitmap(bitmap);
                new UpdateImage().execute();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
/*    public void smsListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(EditProfile.this, "SMS Retriever Started", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onOtpReceived(String otp) {
        Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        inputOtp.setText(otp);
    }

    @Override
    public void onOtpTimeout() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }*/

    class My_PersonalTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                rs=Connectivity.getLogin(roll_no);
                if(rs.next())
                {
                    myname.setText(rs.getString("name"));
                    Course.setText(rs.getString("dept_name"));
                    int temp=rs.getInt("Sem");
                    Sem.setText(temp+"");
                    gender.setText(rs.getString("Gender"));
                    Start_Session.setText(rs.getString("Start_Session"));
                    End_Session.setText(rs.getString("End_Session"));
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
        }
    }
    class UpdateTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                res=Connectivity.UpdateDetails(Start_Session.getText().toString(), End_Session.getText().toString(),gender.getText().toString() , roll_no);
            }
            catch (Exception e)
            {

            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.isEmpty())
            {
                Toast.makeText(EditProfile.this, "Not Updated", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void SetValidation()
    {
        if (pass1.getText().toString().isEmpty()&&pass2.getText().toString().isEmpty()) {
            pass1.setError(getResources().getString(R.string.password_error));
            pass2.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (pass2.getText().length() < 6) {
            pass2.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }else if(!pass2.getText().toString().equals(pass3.getText().toString()))
        {
            pass2.setError(getResources().getString(R.string.error_password_match));
            isPasswordValid=false;
        }
        else  {
            isPasswordValid = true;
        }
    }
    class UpdatePassword extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {
                res=Connectivity.UpdatePassword(roll_no,pass1.getText().toString(),pass2.getText().toString());
            }
            catch (Exception e)
            {

            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.isEmpty())
            {
                Toast.makeText(EditProfile.this, "Wrong Current Password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditProfile.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class UpdateImage extends AsyncTask<String,Void,String>
    {
        InputStream imageStream = null;
        int imageFileLength;
        String resultSet;
        @Override
        protected String doInBackground(String... strings) {

            //System.out.println("Image File length : "+ imageFileLength);
            try {
                File imageFile = new File(picturePath);
                imageFileLength = (int) imageFile.length();
                imageStream = new FileInputStream(imageFile);
                resultSet=Connectivity.UploadImage(imageStream,imageFileLength,roll_no );
            } catch (Exception e) {
                e.printStackTrace();
                resultSet=e.toString();
            }
            return resultSet;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.isEmpty())
            {
                Toast.makeText(EditProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditProfile.this, "Error"+s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
