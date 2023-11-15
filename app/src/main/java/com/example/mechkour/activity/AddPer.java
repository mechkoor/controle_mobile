package com.example.mechkour.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mechkour.R;
import com.example.mechkour.beans.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddPer extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerSpe;
    private EditText nom, prenom, date;
    List<String> speList = new ArrayList<>();
    Service spe;
    String selectedSpe;
    private Button bnAdd;
    List<Service> spes = new ArrayList<>();
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.114:8080/api/employe/save";
    String getspe = "http://192.168.1.114:8080/api/service/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_per);

        spinnerSpe = findViewById(R.id.spe);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);

        date = findViewById(R.id.date);
        bnAdd = findViewById(R.id.add);

        bnAdd.setOnClickListener(this);
        loadspe();

        spinnerSpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpe = (String) spinnerSpe.getSelectedItem();
                for (Service r : spes) {
                    if (r.getNom().equals(selectedSpe)) {
                        spe = new Service(r.getId(), r.getNom());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadspe() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getspe,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String code = jsonObject.getString("nom");

                                spes.add(new Service(id, code));
                                speList.add(code);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddPer.this, android.R.layout.simple_spinner_item, speList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSpe.setAdapter(spinnerAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nom", nom.getText().toString());
            jsonBody.put("prenom", prenom.getText().toString());
            jsonBody.put("date", date.getText().toString());
            JSONObject speObj = new JSONObject();
            speObj.put("id", spe.getId());
            speObj.put("nom", spe.getNom());
            jsonBody.put("service", speObj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AddPer.this, "new prof added !", Toast.LENGTH_SHORT).show();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);

    }
}