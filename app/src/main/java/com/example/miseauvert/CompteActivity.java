package com.example.miseauvert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompteActivity extends AppCompatActivity {

    // Page de selection
    private int id;
    private String tempid;
    private EditText editfraiseventuel;
    private EditText editheureA;
    private EditText editheureD;
    private EditText editrepas;
    private EditText editdateP;

    private float frais_eventuel;
    private String heureA;
    private String heureD;
    private int repas;
    private String dateP;

    private String temprepas;

    private String tempfraiseventuel;

    private Button btnmodif;

    private DatabaseManager databaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        tempid = getIntent().getStringExtra("id_information_garde");
        id = Integer.parseInt(tempid);

        editfraiseventuel = findViewById(R.id.edit_frais_eventuel);
        editheureA = findViewById(R.id.edit_heureA);
        editheureD = findViewById(R.id.edit_heureD);
        editrepas = findViewById(R.id.edit_repas);
        editdateP = findViewById(R.id.edit_dateP);



        databaseManager = new DatabaseManager(getApplicationContext());

        affichercompte();

        // Boutton de modification

        btnmodif = findViewById(R.id.btnmodif);
        btnmodif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                heureA = editheureA.getText().toString();
                tempfraiseventuel = editfraiseventuel.getText().toString();
                frais_eventuel = Float.parseFloat(tempfraiseventuel);
                heureD = editheureD.getText().toString();
                temprepas = editrepas.getText().toString();
                repas = Integer.parseInt(temprepas);
                dateP = editdateP.getText().toString();
                modifiercompte();
            }
        });

    }

    public void onApiresponse(JSONObject response){
        String RheureA;
        String Rfrais_eventuel;
        String RheureD;
        String Rrepas;
        String RdateP;

        try {

            // récupération des valeurs de L'api

            Rfrais_eventuel = response.getString("frais_eventuel");
            RheureA = response.getString("heureA");
            RheureD = response.getString("heureD");
            Rrepas = response.getString("repas");
            RdateP = response.getString("dateP");

            editheureA.setText(RheureA);
            editfraiseventuel.setText(Rfrais_eventuel);
            editheureD.setText(RheureD);
            editrepas.setText(Rrepas);
            editdateP.setText(RdateP);


        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
        }
    }



    public void affichercompte(){
       // String url = "http://192.168.1.37/ApiM3/affichercompte.php";
        String url = "http://10.0.2.2:8888/ApiM3/affichercompte.php";

        // mettre des paramètres

        Map<String, Integer> params = new HashMap<>();
        params.put("id",id);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiresponse(response);
                Toast.makeText(getApplicationContext(), "OPERATION SUCCESSFUL", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        databaseManager.queue.add(jsonObjectRequest);

    }

    public void modifierCompte() {
        // Utilisation de HTTPS au lieu de HTTP pour une communication sécurisée
        String url = "https://10.0.2.2:8888/ApiM3/modifiercompte.php";

        // Création d'un objet JSONObject pour les paramètres de la requête
        JSONObject params = new JSONObject();
        try {
            params.put("heureA", heureA);
            params.put("heureD", heureD);
            params.put("repas", temprepas);
            params.put("frais_eventuel", tempfraiseventuel);
            params.put("dateP", dateP);
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Création de la requête avec Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Gestion de la réponse réussie
                Toast.makeText(getApplicationContext(), "OPERATION SUCCESSFUL", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gestion de l'erreur de réponse
                Toast.makeText(getApplicationContext(), "OPERATION FAILED", Toast.LENGTH_LONG).show();
                Log.e("Volley Error", error.toString());
            }
        });

        // Ajout de la requête à la file d'attente de Volley
        databaseManager.queue.add(jsonObjectRequest);
    }

}