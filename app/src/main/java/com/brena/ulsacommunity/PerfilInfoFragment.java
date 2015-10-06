package com.brena.ulsacommunity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DanielBrena on 25/09/15.
 */
public class PerfilInfoFragment extends Fragment {
    TextView textView;
    ArrayList<String> params;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        params = getArguments().getStringArrayList("marker");
        return inflater.inflate(R.layout.perfil_info_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) getView().findViewById(R.id.informacion);
        textView.setText(this.params.get(3));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
