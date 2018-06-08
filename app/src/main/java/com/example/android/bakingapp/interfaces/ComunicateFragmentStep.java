package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.model.Step;

import java.util.List;

public interface ComunicateFragmentStep {
    void sendStep (List<Step> stepList, int id);
}
