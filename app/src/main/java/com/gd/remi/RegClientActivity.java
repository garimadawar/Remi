package com.gd.remi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static butterknife.ButterKnife.*;

public class RegClientActivity extends AppCompatActivity {

    @InjectView(R.id.editName)
    EditText txtName;
    @InjectView(R.id.editFirmName)
    EditText txtFirmName;
    @InjectView(R.id.editPhone)
    EditText txtPhone;
    @InjectView(R.id.editEmail)
    EditText txtEmail;
    @InjectView(R.id.editAge)
    EditText txtAge;
    @InjectView(R.id.editAddress)
    EditText txtAddress;

    @InjectView(R.id.editCity)
    EditText txtCity;


    @InjectView(R.id.buttonSubmit)
    Button btnSubmit;

    Client rcvClient;
    Client client = new Client();
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    boolean updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_client);

        ButterKnife.inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(this);
        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyClient");


        if(updateMode){
            rcvClient = (Client) rcv.getSerializableExtra("keyClient");
            txtName.setText(rcvClient.getName());
            txtFirmName.setText(rcvClient.getFirm_Name());
            txtPhone.setText(rcvClient.getPhoneNo());
            txtEmail.setText(rcvClient.getEmail());
            txtAge.setText(rcvClient.getAge());
            txtAddress.setText(rcvClient.getAddress());
            txtCity.setText(rcvClient.getCity());

            btnSubmit.setText("Update");
        } }
    public void clickHandler(View view){
        if(view.getId() == R.id.buttonSubmit){

            client.setName(txtName.getText().toString().trim());
            client.setName(txtFirmName.getText().toString().trim());
            client.setPhoneNo(txtPhone.getText().toString().trim());
            client.setEmail(txtEmail.getText().toString().trim());
            client.setAge(txtAge.getText().toString().trim());
            client.setAddress(txtAddress.getText().toString().trim());
            client.setCity(txtCity.getText().toString().trim());


            if(validateFields()) {
                if (isNetworkConnected())
                    insertIntoCloud();
                else
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Please correct Input", Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean isNetworkConnected() {

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());

    }

    void clearFields() {
        txtName.setText("");
        txtFirmName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        txtCity.setText("");


    }

    void insertIntoCloud() {
        String url = "";

        if (!updateMode) {
            url = Util.INSERT_CLIENT_PHP;
        } else {
            url = Util.UPDATE_CLIENT_PHP;
        }

        progressDialog.show();
       // Log.e("user",client.toString());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){
                        Toast.makeText(RegClientActivity.this,message,Toast.LENGTH_LONG).show();

                        if(updateMode)
                            finish();

                    }else{
                        Toast.makeText(RegClientActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(RegClientActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(RegClientActivity.this, "Success"+response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegClientActivity.this, "Some Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                if (updateMode)
                    map.put("Cid", String.valueOf(rcvClient.getCid()));

                map.put("Name", client.getName());
                map.put("Firm_Name", client.getFirm_Name());
                map.put("PhoneNo", client.getPhoneNo());
                map.put("Email", client.getEmail());
                map.put("Age", client.getAge());
                map.put("Address",client.getAddress());
                map.put("City", client.getCity());
                return map;
            }
        };

        requestQueue.add(request); // execute the request, send it ti server

        clearFields();
    }

    boolean validateFields() {
        boolean flag = true;

        if (client.getName().isEmpty()) {
            flag = false;
            txtName.setError("Please Enter Name");
        }

        if (client.getAge().isEmpty()) {
            flag = false;
            txtAge.setError("Please Enter Age");
        }
        if (client.getAddress().isEmpty()) {
            flag = false;
            txtAddress.setError("Please Enter Address");
        }

        if (client.getCity().isEmpty()) {
            flag = false;
            txtCity.setError("Please Enter City");
        }
        if (client.getFirm_Name().isEmpty()) {
            flag = false;
            txtCity.setError("Please Enter Firm Name");
        }

        if (client.getPhoneNo().isEmpty()) {
            flag = false;
            txtPhone.setError("Please Enter Phone");
        } else {
            if (client.getPhoneNo().length() < 10) {
                flag = false;
                txtPhone.setError("Please Enter 10 digits Phone Number");
            }
        }

        if (client.getEmail().isEmpty()) {
            flag = false;
            txtEmail.setError("Please Enter Email");
        } else {
            if (!(client.getEmail().contains("@") && client.getEmail().contains("."))) {
                flag = false;
                txtEmail.setError("Please Enter correct Email");
            }
        }
        return flag;
    }
}