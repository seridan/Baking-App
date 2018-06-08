package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailFragmentAdapter;
import com.example.android.bakingapp.interfaces.ComunicateFragmentStep;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class DetailFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEP = "step";
    private static final String ARG_VIDEO = "video";
    private Recipe selectedRecipe;
    private List<Step> mListSteps;


    @BindView(R.id.step_rv) RecyclerView mRecyclerSteps;

    Activity mActivity;
    ComunicateFragmentStep comunicateFragmentStep;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // Inicializo el arrayList en el onCreateView para que no me de error
        mListSteps = new ArrayList<>();

        ButterKnife.bind(this, rootView);
        Timber.plant(new Timber.DebugTree());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerSteps.setLayoutManager(layoutManager);
        mRecyclerSteps.setHasFixedSize(true);
        DetailFragmentAdapter mDetailFragmentAdapter = new DetailFragmentAdapter(mListSteps);
        mRecyclerSteps.setAdapter(mDetailFragmentAdapter);
        //Creo un objeto bundle para obtener el que he enviado desde el fragment RecipeMainFragment.
        final Bundle recipeBundle = getArguments();

        if (recipeBundle != null){
            //Almaceno en el objeto de la clase Recipe, el objeto de la misma clase que he recibido
            //a través del bundle y que como key es la constante ARG_RECIPE, que debe de ser la misma
            //que se le asignó desde el origen.
            selectedRecipe = recipeBundle.getParcelable(ARG_RECIPE);
            if (selectedRecipe != null) {
                //Si no es null almacenamos el array de steps en la variable mSteps
                mListSteps = selectedRecipe.getSteps();
                //y se la pasamos al adapter mediante el método setStepList de la clase;
                mDetailFragmentAdapter.setStepList(mListSteps);
            }
        }

        //Activo el evento onClick
        mDetailFragmentAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = mListSteps.get(mRecyclerSteps.getChildAdapterPosition(view)).getId();
                comunicateFragmentStep.sendStep(mListSteps, id);
               //setArgStep(view);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            mActivity = (Activity) context;
            comunicateFragmentStep = (ComunicateFragmentStep) mActivity;
        }
    }

    private void setArgStep (View view) {

        //Obtengo el paso seleccionado y lo almaceno en la variable de tipo string.
        String selectedStepDescription = mListSteps.get
                (mRecyclerSteps.getChildAdapterPosition(view)).getDescription();

        //Obtengo la url del video seleccionado y lo almaceno en la variable de tipo string.
        String selectedStepVideoUrl = mListSteps.get
                (mRecyclerSteps.getChildAdapterPosition(view)).getVideoURL();
        //Compruebo que la url no sea null, si lo es obtengo la url del campo thumbnailUrl.
        if (selectedStepVideoUrl == null || selectedStepVideoUrl.isEmpty()){
            selectedStepVideoUrl = mListSteps.get
                    (mRecyclerSteps.getChildAdapterPosition(view)).getThumbnailURL();
        }
        //Creo un nuevo bundle para almacenar el paso y el video seleccionados
        Bundle bundleStep = new Bundle();
        //Añado el paso seleccionado al bundle.
        bundleStep.putString(ARG_STEP, selectedStepDescription);
        //Añado el video seleccionado al bundle.
        bundleStep.putString(ARG_VIDEO, selectedStepVideoUrl);
        //Creo un nuevo fragment del tipo Media que es el que quiero que se muestre
        MediaFragment mediaFragment = new MediaFragment();
        //Inserto el bundle como argumentos.
        mediaFragment.setArguments(bundleStep);
        //Por medio de getActivity puedo acceder al fragmentManager
        getActivity().getSupportFragmentManager().beginTransaction()
                //Como contenedor utilizo el mismo que tengo en el layout del activity_main.xml
                .replace(R.id.fragment_list_container, mediaFragment)
                .addToBackStack(null)
                .commit();
    }

}
