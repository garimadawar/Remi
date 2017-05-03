package com.gd.remi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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

import static com.gd.remi.R.drawable.b;
import static com.gd.remi.R.id.txtPhone;

public class EmpActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @InjectView(R.id.editName)
    EditText txtName;
    @InjectView(R.id.editPhone)
    EditText txtPhone;
    @InjectView(R.id.editAge)
    EditText txtAge;
    @InjectView(R.id.editWage)
    EditText txtWage;
    @InjectView(R.id.editAddress)
    EditText txtAddress;
    @InjectView(R.id.radioButtonMale)
    RadioButton radioButtonMale;
    @InjectView(R.id.radioButtonFemale)
    RadioButton radioButtonFemale;

    @InjectView(R.id.button)

    Button btnSubmit;

    Employee rcvEmployee;
    Employee employee=new Employee();
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    boolean updateMode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp);
        ButterKnife.inject(this);


radioButtonMale.setOnCheckedChangeListener(this);
        radioButtonFemale.setOnCheckedChangeListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        requestQueue= Volley.newRequestQueue(this);
        Intent rcv=getIntent();
        updateMode=rcv.hasExtra("keyEmployee");

        if(updateMode){
            rcvEmployee=(Employee)rcv.getSerializableExtra("KeyEmployee");
            txtName.setText(rcvEmployee.getName());
            txtPhone.setText(rcvEmployee.getPhoneNo());
            txtAge.setText(rcvEmployee.getAge());
            txtWage.setText(rcvEmployee.getWage());
            txtAddress.setText(rcvEmployee.getAddress());
            radioButtonMale.setText(rcvEmployee.getGender());
            radioButtonFemale.setText(rcvEmployee.getGender());
            btnSubmit.setText("Update");
        }}


    public void clickHandler(View view) {
        if (view.getId() == R.id.buttonSubmit) {
            employee.setName(txtName.getText().toString().trim());
            employee.setPhoneNo(txtPhone.getText().toString().trim());
            employee.setAge(txtAge.getText().toString().trim());
            employee.setWage(txtWage.getText().toString().trim());
            employee.setAddress(txtAddress.getText().toString().trim());
            if(rcvEmployee.getGender().equals("Male")){
                radioButtonMale.setChecked(true);
            }else{
                radioButtonFemale.setChecked(true);
            }
        }
        if(validateFields()) {
            if (isNetworkConnected())
                insertIntoCloud();
            else
                Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Please correct Input", Toast.LENGTH_LONG).show();
        }
    }

            void clearFields(){
                txtName.setText("");
                txtPhone.setText("");
                txtAge.setText("");
                txtWage.setText("");
                txtAddress.setText("");
                radioButtonMale.setSelected(false);
                radioButtonFemale.setSelected(false);
            }
    boolean isNetworkConnected(){

        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        return (networkInfo!=null && networkInfo.isConnected());

    }



    void insertIntoCloud(){
        String url = "";

        if (!updateMode) {
            url = Util.INSERT_EMPLOYEE_PHP;
        } else {
            url = Util.UPDATE_EMPLOYEE_PHP;
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
                        Toast.makeText(EmpActivity.this,message,Toast.LENGTH_LONG).show();

                        if(updateMode)
                            finish();

                    }else{
                        Toast.makeText(EmpActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(EmpActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(EmpActivity.this, "Success"+response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EmpActivity.this, "Some Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                if (updateMode)
                    map.put("Eid", String.valueOf(rcvEmployee.getEid()));

                map.put("Name", employee.getName());
                map.put("PhoneNo", employee.getPhoneNo());
                map.put("Age",employee .getAge());
                map.put("Wage",employee .getWage());
                map.put("Address",employee.getAddress());
                map.put("Gender",employee.getGender());
                return map;
            }
        };

        requestQueue.add(request); // execute the request, send it ti server

        clearFields();

    }
    boolean validateFields() {
        boolean flag = true;

        if (employee.getName().isEmpty()) {
            flag = false;
            txtName.setError("Please Enter Name");
        }
        if(employee.getGender().isEmpty()){
            flag=false;
            radioButtonMale.setError("Select Gender");
            radioButtonFemale.setError("Select Gender");
        }

        if (employee.getAge().isEmpty()) {
            flag = false;
            txtAge.setError("Please Enter Age");
        }
        if (employee.getAddress().isEmpty()) {
            flag = false;
            txtAddress.setError("Please Enter Address");
        }

        if (employee.getPhoneNo().isEmpty()) {
            flag = false;
            txtPhone.setError("Please Enter Phone");
        } else {
            if (employee.getPhoneNo().length() < 10) {
                flag = false;
                txtPhone.setError("Please Enter 10 digits Phone Number");
            }
        }


        return flag;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        int id = compoundButton.getId();

        if(b) {
            if (id == R.id.radioButtonMale) {
                employee.setGender("Male");
            } else {
                employee.setGender("Female");
            }
        }
    }
}
