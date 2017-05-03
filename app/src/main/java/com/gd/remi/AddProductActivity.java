package com.gd.remi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AddProductActivity extends AppCompatActivity {
    @InjectView(R.id.editName)
    EditText txtName;
    @InjectView(R.id.editPrice)
    EditText txtPrice;
    @InjectView(R.id.editTextdescription)
    EditText txtDesc;

    Product rcvProduct;
    @InjectView(R.id.buttonSubmit)
    Button btnSubmit;

    Product product = new Product();
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    boolean updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ButterKnife.inject(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(this);
        Intent rcv =getIntent();
        updateMode = rcv.hasExtra("keyProduct");

        rcvProduct = (Product) rcv.getSerializableExtra("keyProduct");
        txtName.setText(rcvProduct.getName());
        txtPrice.setText(rcvProduct.getPrice());
        txtDesc.setText(rcvProduct.getDesc());

        btnSubmit.setText("update");



    }
    if(updateMode){
        rcvProduct = (Product) rcv.getSerializableExtra("keyProduct");
        txtName.setText(rcvClient.getName());


        btnSubmit.setText("Update");
    }
    public void clickHandler(View view){
        if(view.getId() == R.id.buttonSubmit){

            product.setName(txtName.getText().toString().trim());
            product.setDesc(txtDesc.getText().toString().trim());
            product.setPrice(txtPrice.getText().toString().trim());



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
        txtPrice.setText("");
        txtDesc.setText("");

    }

    void insertIntoCloud() {
        String url = "";

        if (!updateMode) {
            url = Util.INSERT_PRODUCT_PHP;
        } else {
            url = Util.UPDATE_PRODUCT_PHP;
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
                        Toast.makeText(AddProductActivity.this,message,Toast.LENGTH_LONG).show();

                        if(updateMode)
                            finish();

                    }else{
                        Toast.makeText(AddProductActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(AddProductActivity.this, "Success"+response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddProductActivity.this, "Some Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                if (updateMode)
                    map.put("Cid", String.valueOf(rcvProduct.getPid()));

                map.put("Name", product.getName());

                map.put("Description", product.getCity());
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
