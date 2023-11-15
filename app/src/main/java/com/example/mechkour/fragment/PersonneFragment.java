package com.example.mechkour.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mechkour.R;
import com.example.mechkour.activity.AddPer;
import com.example.mechkour.adapter.PersoneAdapter;
import com.example.mechkour.beans.Personne;
import com.example.mechkour.beans.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PersonneFragment extends Fragment {

    private static final String URL_LOAD = "http://192.168.1.114:8080/api/employe/all";

    private ListView listView;
    private List<Personne> personneList;
    private String nom;


    private Date date;
    private String ser;
    private JSONObject service;
    private Date dateER;

    Service serr;

    private PersoneAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personne, container, false);

        listView = view.findViewById(R.id.list);
        personneList = new ArrayList<>();
        adapter = new PersoneAdapter(getActivity(), R.layout.item11,personneList);
        listView.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.image);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddPer.class);
                startActivity(intent);
            }
        });
        loadProfs();
        return view;

    }

    private void loadProfs() {
        personneList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_LOAD,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String nom = jsonObject.getString("nom");
                                String date = jsonObject.getString("date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                                try {
                                    dateER = dateFormat.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();

                                    dateER = null;
                                }
                                service = jsonObject.getJSONObject("service");
                                int speId = service.getInt("id");
                                String speCodeCode = service.getString("nom");

                                serr =new Service( speId ,speCodeCode );

                                Personne prof = new  Personne(id, nom,dateER,serr);
                                personneList.add(prof);
                                adapter.add(prof);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestion des erreurs
                    }
                }
        );

        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }


}