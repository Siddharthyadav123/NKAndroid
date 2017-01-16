package com.netkoin.app.controller;

import com.netkoin.app.models.ModelFacade;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class AppController {
    public static AppController instance;

    private ModelFacade modelFacade;

    private AppController() {
        modelFacade = new ModelFacade();
    }

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public ModelFacade getModelFacade() {
        return modelFacade;
    }
}
