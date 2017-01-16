package com.netkoin.app.models;

/**
 * Created by siddharthyadav on 02/01/17.
 */

public class ModelFacade {
    private LocalModel localModel;
    private RemoteModel remoteModel;

    // intialize all your member variables here.
    public ModelFacade() {
        this.localModel = new LocalModel();
        this.remoteModel = new RemoteModel();
    }

    public LocalModel getLocalModel() {
        return localModel;
    }

    public RemoteModel getRemoteModel() {
        return remoteModel;
    }
}
