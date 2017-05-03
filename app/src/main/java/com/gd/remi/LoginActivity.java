package com.gd.remi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.R.attr.id;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtName, txtPass;
    Button btnLog, btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inItViews();
    }

    void inItViews() {
        txtName = (EditText) findViewById(R.id.editName);
        txtPass = (EditText) findViewById(R.id.editPass);
        btnLog = (Button) findViewById(R.id.buttonLog);
        btnNew = (Button) findViewById(R.id.buttonNew);
        btnLog.setOnClickListener(this);
        btnNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonLog) {

            String name = txtName.getText().toString().trim();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            intent.putExtra("keyName", name);
            startActivity(intent);

        } else if (id == R.id.buttonNew) {
            Intent intent = new Intent(LoginActivity.this, RegClientActivity.class);
            startActivity(intent);
        }
    }

}