package com.project.grace.floodmeterapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Graph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart lineCart;
    private static String TAG = "Rain Gauge Chart";
    private static LineData lineData;
    private static BarData barData;
    private static InputStream iStream;
    private static BufferedReader reader;
    private static CombinedData graphData;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "June"
    };
    private boolean isDataConstant = true;

    private OnFragmentInteractionListener mListener;

    public static Graph getInstance() {
        return new Graph();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineCart = (LineChart) view.findViewById(R.id.line_chart);
//        populateChart();
        JSONParser viewData = new JSONParser();
        viewData.execute();
        return view;
    }

    private void populateChart() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));
        entries.add(new BarEntry(7f, 4));
        entries.add(new BarEntry(3f, 5));

        LineDataSet set1 = new LineDataSet(entries, "Rain Gauge");
        set1.setFillAlpha(110);
        set1.setLineWidth(2.5f);
        set1.setColor(Color.rgb(28, 28, 26));
        set1.setCircleColor(Color.rgb(240, 238, 70));
        set1.setCircleRadius(2f);
        set1.setFillColor(Color.rgb(240, 238, 70));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawValues(true);
        set1.setValueTextSize(5f);
        set1.setValueTextColor(Color.rgb(240, 238, 70));
        set1.setLineWidth(2f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData lineData = new LineData(dataSets);

        lineCart.animateXY(3000, 3000);
        lineCart.setVisibleXRangeMaximum(200); // allow 20 values to be displayed at once on the x-axis, not more
        lineCart.moveViewToX(10);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class JSONParser extends AsyncTask<Void, Void, Void> {

        private String strUrl = "http://philsensors.asti.dost.gov.ph/php/24hrs.php?stationid=954&fbclid=IwAR3uykazMjdYDWTvFFjADihonicb6hh57Fb1CHCfhbXTNjQ45WCbLjGNYoo";
        private URL url = null;
        private String data = "";
        private StringBuffer sb = new StringBuffer();
        double result = 0;
        HttpURLConnection connection;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            //            <editor-fold desc="Thrad for rainfall API">
            try {
                url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                iStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(iStream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data += line;
                }
                //For Series
                //data = "{\"series\": [{\"maxval\": 0.0, \"sensor_id\": 2, \"label\": \"Rainfall\", \"values\": [0.0, 0.0, 0.0, 0.0, 60, 50, 40, 30, 20, 10, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 20, 40, 0.0, 0.0, 0.0, 0.0, 0.0, 40, 50, 10, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], \"plot_bands\": [{\"color\": \"rgba(0, 197, 205, 0.6)\", \"text\": \"Light\", \"from\": 0.0, \"to\": 0.625}, {\"color\": \"rgba(11, 70, 252, 0.6)\", \"text\": \"Moderate\", \"from\": 0.625, \"to\": 1.875}, {\"color\": \"rgba(1, 1, 182, 0.6)\", \"text\": \"Heavy\", \"from\": 1.875, \"to\": 3.0}, {\"color\": \"rgba(255, 176, 15, 0.6)\", \"text\": \"Intense\", \"from\": 3.0, \"to\": 7.0}, {\"color\": \"rgba(255, 36, 0, 0.6) \", \"text\": \"Torrential\", \"from\": 7.0, \"to\": 10.0}], \"units\": \"mm\"}], \"station\": {\"url\": \"/api/station/2/1503\", \"station_id\": 1503, \"station_type_id\": 2, \"lat\": 7.084133, \"lng\": 125.508112, \"verbose_name\": \"Pshs-Smc Mintal, Davao City, Davao City\"}, \"partition\": 4, \"time\": [\"Jul 19, 2018 05:15 PM\", \"Jul 19, 2018 05:30 PM\", \"Jul 19, 2018 05:45 PM\", \"Jul 19, 2018 06:00 PM\", \"Jul 19, 2018 06:15 PM\", \"Jul 19, 2018 06:30 PM\", \"Jul 19, 2018 06:45 PM\", \"Jul 19, 2018 07:30 PM\", \"Jul 19, 2018 07:45 PM\", \"Jul 19, 2018 08:00 PM\", \"Jul 19, 2018 08:15 PM\", \"Jul 19, 2018 08:30 PM\", \"Jul 19, 2018 08:45 PM\", \"Jul 19, 2018 09:00 PM\", \"Jul 19, 2018 09:15 PM\", \"Jul 19, 2018 09:30 PM\", \"Jul 19, 2018 09:45 PM\", \"Jul 19, 2018 10:15 PM\", \"Jul 19, 2018 10:30 PM\", \"Jul 19, 2018 10:45 PM\", \"Jul 19, 2018 11:00 PM\", \"Jul 19, 2018 11:30 PM\", \"Jul 19, 2018 11:45 PM\", \"Jul 20, 2018 12:00 AM\", \"Jul 20, 2018 12:45 AM\", \"Jul 20, 2018 01:30 AM\", \"Jul 20, 2018 02:00 AM\", \"Jul 20, 2018 02:15 AM\", \"Jul 20, 2018 02:30 AM\", \"Jul 20, 2018 02:45 AM\", \"Jul 20, 2018 03:00 AM\", \"Jul 20, 2018 03:15 AM\", \"Jul 20, 2018 03:30 AM\", \"Jul 20, 2018 04:15 AM\", \"Jul 20, 2018 04:30 AM\", \"Jul 20, 2018 04:45 AM\", \"Jul 20, 2018 05:00 AM\", \"Jul 20, 2018 05:15 AM\", \"Jul 20, 2018 05:30 AM\", \"Jul 20, 2018 05:45 AM\", \"Jul 20, 2018 06:00 AM\", \"Jul 20, 2018 06:15 AM\", \"Jul 20, 2018 06:30 AM\", \"Jul 20, 2018 06:45 AM\", \"Jul 20, 2018 07:00 AM\", \"Jul 20, 2018 07:15 AM\", \"Jul 20, 2018 07:30 AM\", \"Jul 20, 2018 07:45 AM\", \"Jul 20, 2018 08:00 AM\", \"Jul 20, 2018 08:15 AM\", \"Jul 20, 2018 08:30 AM\", \"Jul 20, 2018 08:45 AM\", \"Jul 20, 2018 09:00 AM\", \"Jul 20, 2018 09:15 AM\", \"Jul 20, 2018 09:30 AM\", \"Jul 20, 2018 09:45 AM\", \"Jul 20, 2018 10:00 AM\", \"Jul 20, 2018 10:15 AM\", \"Jul 20, 2018 10:30 AM\", \"Jul 20, 2018 10:45 AM\", \"Jul 20, 2018 11:00 AM\", \"Jul 20, 2018 11:15 AM\", \"Jul 20, 2018 11:30 AM\", \"Jul 20, 2018 11:45 AM\", \"Jul 20, 2018 12:00 PM\", \"Jul 20, 2018 12:15 PM\", \"Jul 20, 2018 12:30 PM\", \"Jul 20, 2018 12:45 PM\", \"Jul 20, 2018 01:00 PM\", \"Jul 20, 2018 01:15 PM\", \"Jul 20, 2018 01:30 PM\", \"Jul 20, 2018 02:15 PM\", \"Jul 20, 2018 02:45 PM\", \"Jul 20, 2018 03:00 PM\", \"Jul 20, 2018 03:30 PM\", \"Jul 20, 2018 03:45 PM\", \"Jul 20, 2018 04:15 PM\"]}";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("First Exception");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (data.equals("")) {
                        data = "{\"series\": [{\"maxval\": 0.0, \"sensor_id\": 2, \"label\": \"Rainfall\", \"values\": [20, 40, 0.0, 0.0, 0.0, 0.0, 0.0, 40, 50, 10, 0.0, 0.0, 65.0, 45.10, 39.0, 20.0, 5.0, 0.0, 0.0, 0.0], \"plot_bands\": [{\"color\": \"rgba(0, 197, 205, 0.6)\", \"text\": \"Light\", \"from\": 0.0, \"to\": 0.625}, {\"color\": \"rgba(11, 70, 252, 0.6)\", \"text\": \"Moderate\", \"from\": 0.625, \"to\": 1.875}, {\"color\": \"rgba(1, 1, 182, 0.6)\", \"text\": \"Heavy\", \"from\": 1.875, \"to\": 3.0}, {\"color\": \"rgba(255, 176, 15, 0.6)\", \"text\": \"Intense\", \"from\": 3.0, \"to\": 7.0}, {\"color\": \"rgba(255, 36, 0, 0.6) \", \"text\": \"Torrential\", \"from\": 7.0, \"to\": 10.0}], \"units\": \"mm\"}], \"station\": {\"url\": \"/api/station/2/1503\", \"station_id\": 1503, \"station_type_id\": 2, \"lat\": 7.084133, \"lng\": 125.508112, \"verbose_name\": \"Pshs-Smc Mintal, Davao City, Davao City\"}, \"partition\": 4, \"time\": [\"Jul 19, 2018 05:15 PM\", \"Jul 19, 2018 05:30 PM\", \"Jul 19, 2018 05:45 PM\", \"Jul 19, 2018 06:00 PM\", \"Jul 19, 2018 06:15 PM\", \"Jul 19, 2018 06:30 PM\", \"Jul 19, 2018 06:45 PM\", \"Jul 19, 2018 07:30 PM\", \"Jul 19, 2018 07:45 PM\", \"Jul 19, 2018 08:00 PM\", \"Jul 19, 2018 08:15 PM\", \"Jul 19, 2018 08:30 PM\", \"Jul 19, 2018 08:45 PM\", \"Jul 19, 2018 09:00 PM\", \"Jul 19, 2018 09:15 PM\", \"Jul 19, 2018 09:30 PM\", \"Jul 19, 2018 09:45 PM\", \"Jul 19, 2018 10:15 PM\", \"Jul 19, 2018 10:30 PM\", \"Jul 19, 2018 10:45 PM\", \"Jul 19, 2018 11:00 PM\", \"Jul 19, 2018 11:30 PM\", \"Jul 19, 2018 11:45 PM\", \"Jul 20, 2018 12:00 AM\", \"Jul 20, 2018 12:45 AM\", \"Jul 20, 2018 01:30 AM\", \"Jul 20, 2018 02:00 AM\", \"Jul 20, 2018 02:15 AM\", \"Jul 20, 2018 02:30 AM\", \"Jul 20, 2018 02:45 AM\", \"Jul 20, 2018 03:00 AM\", \"Jul 20, 2018 03:15 AM\", \"Jul 20, 2018 03:30 AM\", \"Jul 20, 2018 04:15 AM\", \"Jul 20, 2018 04:30 AM\", \"Jul 20, 2018 04:45 AM\", \"Jul 20, 2018 05:00 AM\", \"Jul 20, 2018 05:15 AM\", \"Jul 20, 2018 05:30 AM\", \"Jul 20, 2018 05:45 AM\", \"Jul 20, 2018 06:00 AM\", \"Jul 20, 2018 06:15 AM\", \"Jul 20, 2018 06:30 AM\", \"Jul 20, 2018 06:45 AM\", \"Jul 20, 2018 07:00 AM\", \"Jul 20, 2018 07:15 AM\", \"Jul 20, 2018 07:30 AM\", \"Jul 20, 2018 07:45 AM\", \"Jul 20, 2018 08:00 AM\", \"Jul 20, 2018 08:15 AM\", \"Jul 20, 2018 08:30 AM\", \"Jul 20, 2018 08:45 AM\", \"Jul 20, 2018 09:00 AM\", \"Jul 20, 2018 09:15 AM\", \"Jul 20, 2018 09:30 AM\", \"Jul 20, 2018 09:45 AM\", \"Jul 20, 2018 10:00 AM\", \"Jul 20, 2018 10:15 AM\", \"Jul 20, 2018 10:30 AM\", \"Jul 20, 2018 10:45 AM\", \"Jul 20, 2018 11:00 AM\", \"Jul 20, 2018 11:15 AM\", \"Jul 20, 2018 11:30 AM\", \"Jul 20, 2018 11:45 AM\", \"Jul 20, 2018 12:00 PM\", \"Jul 20, 2018 12:15 PM\", \"Jul 20, 2018 12:30 PM\", \"Jul 20, 2018 12:45 PM\", \"Jul 20, 2018 01:00 PM\", \"Jul 20, 2018 01:15 PM\", \"Jul 20, 2018 01:30 PM\", \"Jul 20, 2018 02:15 PM\", \"Jul 20, 2018 02:45 PM\", \"Jul 20, 2018 03:00 PM\", \"Jul 20, 2018 03:30 PM\", \"Jul 20, 2018 03:45 PM\", \"Jul 20, 2018 04:15 PM\"]}";
                        isDataConstant = true;
                        //populated data
                        JSONArray values = new JSONObject(new JSONObject(data).getJSONArray("series").getString(0)).getJSONArray("values");
                        Random r = new Random();
                        double xArray[] = new double[values.length()];
                        double yArray[] = new double[values.length() - 1];
                        double xySum = 0, xSquareSum = 0;
                        double a = 0, b = 0, yTotal = 0, xTotal = 0;
                        for (int x = 0; x < values.length() - 1; x++) {
                            double randomY = 0 + (60 - 0) * r.nextDouble();
                            xArray[x] = values.getDouble(x);
                            yArray[x] = randomY;
                            xySum += xArray[x] * randomY;
                            xSquareSum += xArray[x] * xArray[x];
                            xTotal += xArray[x];
                            yTotal += randomY;
                        }
                        xArray[values.length() - 1] = values.getDouble(values.length() - 1);
                        xTotal += xArray[values.length() - 1];
                        b = xySum / xSquareSum;
                        a = (yTotal / yArray.length) - (b * (xTotal / xArray.length));
                        result = a + (b * xArray[values.length() - 1]);
                    } else {
                        isDataConstant = false;
                    }

                    JSONObject jsonObject = new JSONObject(data);
                    jsonObject.toString();

                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    //For Values

                    ArrayList<Entry> jsonMap = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String dateRecord = jObject.getString("Datetime Read");
                        float waterLevel = Float.parseFloat(jObject.getString("Waterlevel"));
                        jsonMap.add(new Entry((float) (i + 2), waterLevel));
                        // here you put ean as key and nr as value
                    }

                    LineDataSet set1 = new LineDataSet(jsonMap, "Rain Gauge");
                    set1.setFillAlpha(110);
                    set1.setLineWidth(2.5f);
                    set1.setColor(Color.rgb(66, 103, 178));
                    set1.setCircleColor(Color.rgb(240, 238, 70));
                    set1.setCircleRadius(2f);
                    set1.setFillColor(Color.rgb(240, 238, 70));
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set1.setDrawValues(true);
                    set1.setValueTextSize(5f);
                    set1.setValueTextColor(Color.rgb(240, 238, 70));
                    set1.setLineWidth(2f);
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineData = new LineData();
                    lineData.addDataSet(set1);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    lineData = new LineData(dataSets);
                    lineCart.getAxisLeft().setTextColor(Color.rgb(247, 77, 24));
                    lineCart.getXAxis().setTextColor(Color.rgb(247, 77, 24));
                    lineCart.getLegend().setTextColor(Color.rgb(247, 77, 24));


                    lineCart.setData(lineData);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {//wait for activity to load
//                        @Override
//                        public void run() {
                    //for(BarEntry b : avgScores)
                    //  Log.d("entry", ""+b.getX()+" "+b.getY());
                    lineCart.setData(lineData);
//                            lineCart.invalidate();
//                        }
//                    }, 200);

                    if (iStream != null && reader != null) {
                        reader.close();
                        iStream.close();
                    }
                } catch (JSONException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lineCart.setData(lineData);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lineCart.setData(lineData);
        }
    }

}
