package com.gd.remi;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajesh Kumar Dawar on 02-05-2017.
 */

public class ClientAdapter extends ArrayAdapter<Client> {

    Context context;
    int resource;
    ArrayList<Client> clientList,tempList;

    public ClientAdapter(Context context, int resource,ArrayList objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
      clientList = objects;
        tempList = new ArrayList<>();
        tempList.addAll(clientList);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        View view = null;

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.txtName);
        TextView txtPhoneNo = (TextView)view.findViewById(R.id.txtPhone);

        Client client = clientList.get(position);
        txtName.setText(client.getName());
        //txtGender.setText(student.getGender());
        txtPhoneNo.setText(String.valueOf(client.getCid()));
        return view;
    }
    public void filter(String str){

        clientList.clear();

        if(str.length()==0){
            clientList.addAll(tempList);
        }else{
            for(Client c : tempList){
                if(c.getName().toLowerCase().contains(str.toLowerCase())){
                    clientList.add(c);
                }
            }
        }

        notifyDataSetChanged();
    }
}
