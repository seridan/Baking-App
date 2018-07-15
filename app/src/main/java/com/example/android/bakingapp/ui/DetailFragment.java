package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailFragmentAdapter;
import com.example.android.bakingapp.interfaces.CommunicateFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.widget.BakingAppWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class DetailFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";

    private Recipe selectedRecipe;
    private List<Step> mListSteps;
    private List<Ingredients> mListIngredients;
    private String recipeName;


    @BindView(R.id.step_rv)
    RecyclerView mRecyclerSteps;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.btn_ingredients)
    Button mBtnIngredients;

    Activity mActivity;
    CommunicateFragment mComunicateFragment;

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

        //Before implement the toolbar, check if the activity associated with the fragment its an instance of
        // MainActivity or DebugActivity, since when the test runs get a ClassCastException with AppCompatActivity.
        if (getActivity() instanceof MainActivity) { //
            //If it is not testing then the toolbar will be implemented.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
                Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
            }
        }


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
            //a través de la interfaz desde la clase mainActivity y que como key es la constante ARG_RECIPE, que debe de ser la misma
            //que se le asignó desde el origen.
            selectedRecipe = recipeBundle.getParcelable(ARG_RECIPE);
            if (selectedRecipe != null) {
                //Si no es null almacenamos la lista de steps en la variable mSteps
                mListSteps = selectedRecipe.getSteps();
                //Y ademas almacenamos la lista de ingredientes para luego pasarsela a la clase ingredientFragment junto con el nombre
                //de la receta para ponerla como titulo en la toolbar del fragment ingredientes.
                mListIngredients = selectedRecipe.getIngredients();
                recipeName = selectedRecipe.getRecipeName();

                //y se la pasamos al adapter mediante el método setStepList de la clase;
                mDetailFragmentAdapter.setStepList(mListSteps);
                mToolbar.setTitle(recipeName);
                saveToSharedPreference(mListIngredients, recipeName);
                updateWidget();
            }
        }

        //Activo el evento onClick del recycler view
        mDetailFragmentAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = mListSteps.get(mRecyclerSteps.getChildAdapterPosition(view)).getId();
                mComunicateFragment.sendFragmentStep(mListSteps, id);
                }
        });

        //Activo el listener del boton ingredientes para pasarle al fragment ingredientes la lista de ingredientes seleccionada
        mBtnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComunicateFragment.sendFragmentIngredients(mListIngredients, recipeName);
            }
        });

        return rootView;
    }

    public void saveToSharedPreference(List<Ingredients> ingredientsList, String nameRecipe){
        Activity activity = getActivity();
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(getString(R.string.ingredients_shared_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ingredientsList);
        editor.putString(getString(R.string.ingredients_list_key), json);
        editor.putString(getString(R.string.recipe_mane_key), nameRecipe);
        editor.apply();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            mActivity = (Activity) context;
            mComunicateFragment = (CommunicateFragment) mActivity;

            }
    }

    private void updateWidget() {
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication())
                .getAppWidgetIds(new ComponentName(getActivity().getApplication(), BakingAppWidgetProvider.class));
        BakingAppWidgetProvider myWidget = new BakingAppWidgetProvider();
        myWidget.onUpdate(getContext(), AppWidgetManager.getInstance(getContext()),ids);
    }

}
