package com.antran.android.ecgmonitor;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;



public class GraphFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View myFragmentView;

    private GraphView graph;
    static GraphFragment fragment;
    SharedPreferences sharedPref;


    public static GraphFragment newInstance(String param1, String param2) {
        fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isGraphOpen(){
        return myFragmentView != null && this.isVisible();
    }

    //public GraphFragment() {
    //}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        boolean isNull = graph==null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    public void initialize(LineGraphSeries<DataPoint>[] series){
        graph = (GraphView) myFragmentView.findViewById(R.id.graphView);
        float tmp;
        if(!sharedPref.getBoolean("pref_yAutoScale", true)){
            graph.getViewport().setYAxisBoundsManual(true);
            tmp = Float.parseFloat(sharedPref.getString("pref_y_min", "-200"));
            graph.getViewport().setMinY(tmp);
            tmp = Float.parseFloat(sharedPref.getString("pref_y_max", "200"));
            graph.getViewport().setMaxY(tmp);
        }
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        tmp = Integer.parseInt(sharedPref.getString("pref_windowSize", "200"));
        graph.getViewport().setMaxX(tmp);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(sharedPref.getBoolean("pref_xgridlabels", true));//pref_ygridlabels
        graph.getGridLabelRenderer().setVerticalLabelsVisible(sharedPref.getBoolean("pref_ygridlabels", true));
        int i =0;
        for (LineGraphSeries<DataPoint> sery : series) {
            if (sery != null) {

                graph.addSeries(sery);
            }
            i++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_graph, container, false);

        return myFragmentView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
