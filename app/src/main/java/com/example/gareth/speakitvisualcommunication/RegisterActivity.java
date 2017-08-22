package com.example.gareth.speakitvisualcommunication;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gareth.speakitvisualcommunication.volley.ErrorResponse;
import com.example.gareth.speakitvisualcommunication.volley.VolleyCallBack;
import com.example.gareth.speakitvisualcommunication.volley.VolleyHelp;
import com.example.gareth.speakitvisualcommunication.volley.VolleyRequest;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    EditText Email, confirmEmail, Password, confirmPassword, userName;
    Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");

        Email = (EditText) findViewById(R.id.editText2);
        confirmEmail = (EditText) findViewById(R.id.editText3);
        Password = (EditText) findViewById(R.id.editText5);
        confirmPassword = (EditText) findViewById(R.id.editText4);
        userName = (EditText) findViewById(R.id.editText);
        Register = (Button) findViewById(R.id.button);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userName.getText().toString().equals("")) {
                    userName.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if (Email.getText().toString().equals("")) {
                    Email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if (Password.getText().toString().equals("")) {
                    Password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if (!confirmPassword.getText().toString().equals(Password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if (!confirmEmail.getText().toString().equals(Email.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Emails do not match" , Toast.LENGTH_SHORT).show();
                    confirmEmail.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    userName.getBackground().clearColorFilter();
                    Email.getBackground().clearColorFilter();
                    confirmEmail.getBackground().clearColorFilter();
                    Password.getBackground().clearColorFilter();
                    confirmPassword.getBackground().clearColorFilter();
                }
                //String BASE_URL = "http://awsandroid.eu-west-1.elasticbeanstalk.com/project/getUsername";
                String BASE_URL = "http://10.0.2.2:5000/project/getAccountUsername";
                String url = BASE_URL;

                HashMap<String, String> headers  = new HashMap<>();
                HashMap<String, String> body  = new HashMap<>();

                body.put("username", userName.getText().toString());


                String contentType =  "application/json";
                VolleyRequest request =   new VolleyRequest(RegisterActivity.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                request.serviceJsonCall(new VolleyCallBack(){
                    @Override
                    public void onSuccess(String result){
                        System.out.print("CALLBACK SUCCESS: " + result);
                        if(!result.equals(userName)) {
                            String BASE_URL = "http://10.0.2.2:5000/project/addAccountUsername";
                            String url = BASE_URL;

                            HashMap<String, String> headers  = new HashMap<>();
                            HashMap<String, String> body  = new HashMap<>();


                            body.put("username", userName.getText().toString());
                            body.put("email" , confirmEmail.getText().toString());
                            body.put("password", confirmPassword.getText().toString());


                            String contentType =  "application/json";
                            VolleyRequest request =   new VolleyRequest(RegisterActivity.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                            request.serviceJsonCall(new VolleyCallBack(){
                                @Override
                                public void onSuccess(String result){
                                    System.out.print("CALLBACK SUCCESS: " + result);
                                    Toast.makeText(RegisterActivity.this, "Sucessfully Registered! ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                @Override
                                public void onError(ErrorResponse errorResponse){
                                    System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "The UserName "+result+ " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ErrorResponse errorResponse){
                        System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());

                    }
                });
            }
        });
    }






// voollwy here
//    public void InsertData(){
//
//        if (EditTextEmptyHolder == true && (Email.getText().toString().equals(confirmEmail.getText().toString())) && Password.getText().toString().equals(confirmPassword.getText().toString())){
//
//            SQLiteDataBaseQueryHolder = "INSERT INTO " +SQLiteHelper.TABLE_NAME+ "(email, password, firstName, lastName) VALUES ('" +Email.getText().toString() +"', '"+Password.getText().toString() +"', '" +firstName.getText().toString() +"', '" +lastName.getText().toString()+ "')";
//
//            SQLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);
//
//            SQLiteDatabase.close();
//
//            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    public void CheckEditTextStatus(){
//
//        NameHolder = (firstName.getText().toString() + lastName.getText().toString());
//        EmailHolder = (Email.getText().toString()+ confirmEmail.getText().toString());
//        PasswordHolder = (Password.getText().toString() + confirmPassword.getText().toString());
//
//        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
//            EditTextEmptyHolder = false;
//        } else {
//            EditTextEmptyHolder = true;
//        }
//
//
//    }

//
//    public void ClearOnSubmission(){
//        Email.getText().clear();
//        Password.getText().clear();
//        confirmPassword.getText().clear();
//        confirmEmail.getText().clear();
//        firstName.getText().clear();
//        lastName.getText().clear();
//
//    }

    // volley
//    public void CheckEmailAlreadyExists() {
//
//        SQLiteDatabase = sqLiteHelper.getWritableDatabase();
//
//        cursor = SQLiteDatabase.query(sqLiteHelper.TABLE_NAME, null, " " + sqLiteHelper.Column_Email + "=?", new String[]{Email.getText().toString()}, null, null, null);
//
//        while (cursor.moveToNext()) {
//            if (cursor.isFirst()) {
//                cursor.moveToFirst();
//                F_Result = "Email Found";
//
//                cursor.close();
//            }
//        }
//
//        CheckFinalResult();
//    }
//
//        public void CheckFinalResult(){
//
//        if (F_Result.equalsIgnoreCase("Email Found")){
//            Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show();
//        } else {
//            InsertData();
//        }
//
//        F_Result = "Not Found";
//
//    }





}

