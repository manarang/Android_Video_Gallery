package com.vg;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Spinner sp;
    private String d_sp;
    private ListView lV;
    private ImageView search, exit;
    private String JSON_STRING, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp     = findViewById(R.id.spin);
        search = findViewById(R.id.search);
        exit   = findViewById(R.id.exit);
        lV     = findViewById(R.id.lV);

        String[] cat=getResources().getStringArray(R.array.genre);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }});

        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d_sp = sp.getSelectedItem().toString().substring(0,1);
                alamat = "https://gkikr.net/v/read1.php?id=" + d_sp;
                lV.setAdapter(null);
                getData();
            }});

        lV.setOnItemClickListener(new GoPlay());

        alamat = "https://gkikr.net/v/read.php";
        getData();
    }

    private void getData(){

        class GetData extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                GoIsi();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s =rh.sendGetRequest(alamat, MainActivity.this);
                return s;
            }
        }
        new GetData().execute();

    }

    public void GoIsi(){
        JSONObject jsonObject;
        ArrayList<String> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                String genre  = object.getString("genre");
                String tahun  = object.getString("tahun");
                String judul  = object.getString("judul");
                String tempat = object.getString("tempat");
                list.add(genre + "/" + tahun + "/" + judul + "/" + tempat);
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        lV.setAdapter(adapter);
    }

    class GoPlay implements AdapterView.OnItemClickListener {
        GoPlay() {
        }

        public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
            try {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                String patok = (String) adapter.getItemAtPosition(position);
                intent.putExtra("Pindah", patok);
                MainActivity.this.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

}
