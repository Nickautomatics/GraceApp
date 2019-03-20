package com.project.grace.floodmeterapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BarChart barChart;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String thisDate;
    ArrayList<BarEntry> entries = new ArrayList<>();
    double locale[];
    private TextView txtWeather;
    SpinnerAdapter adapter;
    String weatherInfo;

    private OnFragmentInteractionListener mListener;

    public static StatisticsFragment getInstance(){
        return new StatisticsFragment();
    }

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        locale = (double[]) getArguments().get("locale");

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        Date todayDate = new Date();
        thisDate = df.format(todayDate);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("crowdsource").child(thisDate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Today",  R.drawable.sun));
        list.add(new ItemData("Yesterday", R.drawable.sun));
        list.add(new ItemData("Weekly", R.drawable.sun));
        list.add(new ItemData("Monthly", R.drawable.sun));
        list.add(new ItemData("Yearly", R.drawable.sun));

        Spinner sp = view.findViewById(R.id.chart_spinner);
        adapter = new SpinnerAdapter(this.getActivity(), R.layout.spinner_weather, R.id.txt, list);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);

        populateChart();
        return view;
    }

    private String getYesterdayDateString() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        return df.format(yesterday());
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private void populateChart(){
        barChart.setNoDataText("Please wait while fetching data from database.");
        myRef.addValueEventListener(new ValueEventListener() {
            ArrayList<BarEntry> entries = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int cloudy = 0;
                int lightRain = 0;
                int mediumRain = 0;
                int heavyRain = 0;
                int thunderstorm = 0;
                int unknown = 0;

                CrowdSource rf = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    rf = snapshot.getValue(CrowdSource.class);
                    System.out.println(rf.getUserID());
                    System.out.println(rf.getTag());

                    if(isInsideLocation((float)rf.lat, (float)rf.lon)){
                        switch (rf.getTag()) {
                            case "Cloudy":
                                System.out.println("Cloudy");
                                cloudy++;
                                break;
                            case "Light Rainfall":
                                System.out.println("Light Rain");
                                lightRain++;
                                break;
                            case "Medium Rainfall":
                                System.out.println("Medium Rain");
                                mediumRain++;
                                break;
                            case "Heavy Rainfall":
                                System.out.println("Heavy Rain");
                                heavyRain++;
                                break;
                            case "Thunderstorm":
                                System.out.println("Thunderstorm");
                                thunderstorm++;
                                break;
                            default:
                                System.out.println("Unknown");
                                unknown++;
                        }
                        System.out.println("Sulod ka sa USEP Longitude" + rf.getLat() + " Latitude " + rf.getLon());
                    }else{
                        System.out.println("Gawas ka sa USEP Longitude" + rf.getLat() + " Latitude " + rf.getLon());
                        unknown++;
                    }

                }

                entries.add(new BarEntry(1f, cloudy, "Cloudy"));
                entries.add(new BarEntry(2f, lightRain,"Light Rainfall"));
                entries.add(new BarEntry(3f, mediumRain, "Medium Rainfall"));
                entries.add(new BarEntry(4f, heavyRain,"Heavy Rainfall"));
                entries.add(new BarEntry(5f, thunderstorm, "Thunderstorm"));
                entries.add(new BarEntry(6f, unknown, "Unknown Ratings."));



                if(entries.size() > 0 ){
                    String[] dataLables = new String[]{"Cloudy","Light Rainfall","Medium Rainfall","Heavy Rainfall", "Thunderstorm"};
                    BarDataSet barSet = new BarDataSet(entries, "Statistics");
                    ArrayList<IBarDataSet> barDataSet = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<>();
                    Collections.addAll(labels, dataLables);

//        barSet.setStackLabels(new String[]{"Cloudy","Light Rainfall","Medium Rainfall","Heavy Rainfall", "Thunderstorm"});
                    barSet.setColor(Color.rgb(246, 87, 6));
                    barSet.setValueTextColor(Color.rgb(31,179,230));
                    barSet.setValueTextSize(10f);
                    barSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    barDataSet.add(barSet);

                    BarData barData = new BarData(barDataSet);
                    barData.setValueTextSize(14f);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter((value, axis) -> {
                        int intValue = (int)value;
                        return (labels.size() > intValue && intValue >= 0) ? labels.get(intValue) : "";
                    });

                    Legend legend = barChart.getLegend();
                    legend.setTextColor(Color.rgb(31,179,230));
                    legend.setTextSize(14f);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {//wait for activity to load
                        @Override
                        public void run() {
                            //for(BarEntry b : avgScores)
                            //  Log.d("entry", ""+b.getX()+" "+b.getY());
                            barChart.setData(barData);
                            barChart.setFitBars(true); // make the x-axis fit exactly all bars
                            barChart.invalidate();
                        }
                    }, 200);


                }else{
                    barChart.setNoDataText("No data available!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public boolean isInsideLocation(float testx, float testy)
            {
                float[] vertx = new float[]{
                        7.084118f, 7.086073f, 7.087450f, 7.085497f
                };
                float[] verty = new float[]{
                        125.615469f, 125.617858f, 125.616718f, 125.614337f
                };

                int nvert = 4;
                int i, j;
                boolean c = false;
                for (i = 0, j = nvert-1; i < nvert; j = i++) {
                    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                            (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                        c = !c;
                }
                return c;
            }
        });



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        barChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner s = view.findViewById(R.id.chart_spinner);
        try{
            if (s.getSelectedItem() != null) {
                System.out.println(s.getSelectedItem());
                if(s.getSelectedItem().equals("Today")){
                    myRef = database.getReference("crowdsource").child(thisDate);
                    populateChart();
                    System.out.println("Karon"); }else if(s.getSelectedItem().equals("Yesterday")){
                    myRef = database.getReference("crowdsource").child(getYesterdayDateString());
                    populateChart();
                    System.out.println("Gahapon"); }else if(s.getSelectedItem().equals("Weekly")){
//                    myRef = database.getReference("crowdsource").child(thisDate);
                }else if(s.getSelectedItem().equals("Monthly")){
//                    myRef = database.getReference("crowdsource").child(thisDate);
                }else if(s.getSelectedItem().equals("Yearly")){

                }
            }else{
                myRef = database.getReference("crowdsource").child(thisDate); }System.out.println(String.valueOf(s.getSelectedItemPosition()));
        }catch (Exception e){
            System.out.println("Error");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
