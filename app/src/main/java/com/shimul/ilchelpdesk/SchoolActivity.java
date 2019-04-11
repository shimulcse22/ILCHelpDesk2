package com.shimul.ilchelpdesk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SchoolActivity extends AppCompatActivity {

    private ArrayList<CustomData<List<School>>> customDataList;
    public ArrayAdapter<String> districtAdapter;
    public ArrayAdapter<String> schoolAdapter;

    private Spinner districtSpinner;
    private Spinner schoolSpinner;
    private EditText titleEditText;
    private EditText assaignerEditText;
    private EditText descriEditText;
    private EditText reporterEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        districtSpinner = findViewById(R.id.districtSpinner);
        schoolSpinner = findViewById(R.id.schoolSpinner);
        titleEditText = findViewById(R.id.titleEditText);
        descriEditText = findViewById(R.id.desEditText);
        assaignerEditText = findViewById(R.id.assaignerEditText);
        reporterEditText = findViewById(R.id.reporterEditText);

        //every time when refreshing it will remove previous data
        customDataList = new ArrayList<>();

        //get data from online
        final String dataFromLink = "http://www.foosociety.com/rest/v1/schools";
        // final String dataFromLink = "http://103.234.26.37:8080/shimul/get_model.php";


        new GetDataFromOnline(dataFromLink).execute();


        //adding district spinner handler
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentSelectedDistrict = districtSpinner.getItemAtPosition(position).toString();
                Log.d("districSelected", currentSelectedDistrict);
                schoolAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, getSchoolList(currentSelectedDistrict));
                schoolSpinner.setAdapter(schoolAdapter);
                schoolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void submitButtonClicked(View v){


        long schoolid = Long.parseLong(districtSpinner.getSelectedItem().toString());

        String title =  titleEditText.getText().toString().trim();
        String description =  descriEditText.getText().toString().trim();
        String reporterEmail =  reporterEditText.getText().toString().trim();
        String assigneeEmail =  assaignerEditText.getText().toString().trim();

        TicketRequest model = new TicketRequest();

        model.setSchoolId(schoolid);
        model.setTitle(title);
        model.setDescription(description);
        model.setReporterEmail(reporterEmail);
        model.setAssigneeEmail(assigneeEmail);

        Call<DefaultResponseBody> call = RetrofitClient
                .getInstance().getApi().ticketCreate(model);

        call.enqueue(new Callback<DefaultResponseBody>() {
            @Override
            public void onResponse(Call<DefaultResponseBody> call, Response<DefaultResponseBody> response) {
                DefaultResponseBody dr = new DefaultResponseBody();
                Toast.makeText(SchoolActivity.this,dr.getResponseMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<DefaultResponseBody> call, Throwable t) {
                Toast.makeText(SchoolActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });




    }



    private class GetDataFromOnline extends AsyncTask<Void, Void, Void> {

        private String urlLink;

        public GetDataFromOnline(String urlLink) {
            this.urlLink = urlLink;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SchoolActivity.this,"Getting Data From Server",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            DataDownloader sh = new DataDownloader();

            String url = urlLink;
            String jsonStr = sh.makeServiceCall(url);

            if(jsonStr == null){
                showToastMessage("Can not Load Data From Server!");
                return null;
            }


            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray schools = jsonObj.getJSONArray("items");

                for (int i = 0; i < schools.length(); i++) {
                    JSONObject c = schools.getJSONObject(i);
                    String district = c.getString("id");
                    String school = c.getString("name");
                    customDataList.add(new CustomData(district,school));
                }
            } catch (final JSONException e) {
                showToastMessage("Parsing Error!");
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ArrayList<String> onlyDistricts = new ArrayList<>();
            for(int i=0;i<customDataList.size();i++){
                String currentDistrict = customDataList.get(i).getId();
                boolean alreadyHave = false;
                for(int j=0;j<onlyDistricts.size();j++){
                    if(onlyDistricts.get(j).equals(currentDistrict)){
                        alreadyHave = true;
                        break;
                    }
                }
                if(alreadyHave == false) {
                    onlyDistricts.add(currentDistrict);
                }
            }

            districtAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, onlyDistricts);
            districtSpinner.setAdapter(districtAdapter);

            if(onlyDistricts.size() > 0){
                //show item 0 as selected from the district spinner
                districtSpinner.setSelection(0);

                //now set school according to the district
                schoolAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, getSchoolList(onlyDistricts.get(0)));
                schoolSpinner.setAdapter(schoolAdapter);
            }
            Log.d("done", "yes everything is done");

            //districtAdapter.notifyDataSetChanged();
            //.  schoolAdapter.notifyDataSetChanged();
        }
    }

    public void showToastMessage(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<String> getSchoolList(String district){
        ArrayList<String> ret = new ArrayList<>();
        for(int i=0;i<customDataList.size();i++){
            if(customDataList.get(i).getId().equals(district)){
                ret.add(customDataList.get(i).getName());
            }
        }
        return ret;
    }
}
