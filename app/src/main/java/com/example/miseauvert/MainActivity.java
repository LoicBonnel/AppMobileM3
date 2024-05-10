package com.example.miseauvert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView erreur;
    private EditText editlogin;

    private EditText edit_id_infoG;
    private Button btnModifier;
    private int idInfoG;
    private String tempidInfoG;
    private DatabaseManager databaseManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        erreur = findViewById(R.id.texterror);

        // On récupére les données des btn / input

        edit_id_infoG = findViewById(R.id.edit_id_infoG);

        btnModifier = findViewById(R.id.btnModifier);

        // On récupére les variables de la BDD


        databaseManager = new DatabaseManager(getApplicationContext());

        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempidInfoG = edit_id_infoG.getText().toString();
                idInfoG = Integer.parseInt(tempidInfoG);
                connectUser();
            }
        });
    }

    public void onApiresponse(JSONObject response){
        Boolean success = null;
        String error = "";

        String id_information_garde;

        try {
            success = response.getBoolean("success");

            if (success == true){
                Intent interfaceActivity = new Intent(getApplicationContext(),CompteActivity.class);
                id_information_garde = response.getString("id");


                // Variables qui seront transmises sur la page suivante

                interfaceActivity.putExtra("id_information_garde", id_information_garde);
                startActivity(interfaceActivity);
                finish();
            }else {
                error=response.getString("error");
                erreur.setVisibility(View.VISIBLE);
                erreur.setText(error);
            }

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
        }
    }




public void connectUser(){
    // Utilisation de HTTPS au lieu de HTTP pour une communication sécurisée
    String url = "https://10.0.2.2:8888/ApiM3/SelectInfoG.php";

    // Utilisation de Volley uniquement avec des requêtes GET ou POST sécurisées
    Map<String, String> params = new HashMap<>();
    params.put("id_information_garde", String.valueOf(idInfoG));

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
            Toast.makeText(getApplicationContext(), "OPERATION FAILED", Toast.LENGTH_LONG).show();
        }
    }) {
        // Override de la méthode getHeaders() pour ajouter des en-têtes d'authentification ou de sécurité si nécessaire
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = super.getHeaders();
            if (headers == null || headers.isEmpty()) {
                headers = new HashMap<>();
            }
            // Ajouter des en-têtes d'authentification ou de sécurité ici si nécessaire
            return headers;
        }
    };

    // Ajout de la requête à la file d'attente de Volley
    databaseManager.queue.add(jsonObjectRequest);
}

}











Code implanté dans chaque bouton :
Text(Time(0; 0; Self.Value/1000); "hh:mm:ss")