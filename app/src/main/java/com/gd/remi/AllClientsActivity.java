package com.gd.remi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllClientsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.listView)
    ListView listView;

int pos;

    EditText txtSearch;
    ArrayList<Client> clientList;
    ClientAdapter adapter;
    ProgressDialog progressDialog;
Client client,rcvClient;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clients);

        ButterKnife.inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(this);
    }

    void retrieveFromCloud() {

        progressDialog.show();

        clientList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Util.RETRIEVE_CLIENT_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("students");

                    int id = 0;
                    String n = "", f = "", p = "", e = "", g = "", a= " ", c = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);

                        id = jObj.getInt("Cid");
                        n = jObj.getString("Name");
                        f = jObj.getString("Firm_Name");
                        p = jObj.getString("PhoneNo");
                        e = jObj.getString("Email");
                        g = jObj.getString("Age");
                        a = jObj.getString("Address");
                        c = jObj.getString("city");

                      clientList.add(new Client(id, n,f, p, e, g,a, c));
                    }

                    adapter = new ClientAdapter(AllClientsActivity.this, R.layout.list_item, clientList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(AllClientsActivity.this);

                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(AllClientsActivity.this, "Some Exception", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AllClientsActivity.this, "Some Error", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest); // Execute the Request
    }

    void showOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"View", "Update", "Delete"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        showClient();
                        break;

                    case 1:
                        Intent intent = new Intent(AllClientsActivity.this, RegClientActivity.class);
                                startActivity(intent);
                        break;

                    case 2:
                        deleteClient();
                        break;
                }

            }
        });
    }
    void showClient(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details of "+client.getName());
        builder.setMessage(client.toString());
        builder.setPositiveButton("Done",null);
        builder.create().show();
    }

    void deleteClient(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ client.getName());
        builder.setMessage("Do you wish to Delete?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Code goes here to delete record from DB

               // int j = resolver.delete(Util.STUDENT_URI,where,null);
               deleteClientC();

            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
        pos = i;
        client = clientList.get(i);
        showOptions();
    }

    void deleteClientC(){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.DELETE_CLIENT_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){
                        clientList.remove(pos);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(AllClientsActivity.this,message,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(AllClientsActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(AllClientsActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AllClientsActivity.this,"Some Volley Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",String.valueOf(client.getCid()));
                return map;
            }
        };

        requestQueue.add(request); // Execution of HTTP Request

    }
}