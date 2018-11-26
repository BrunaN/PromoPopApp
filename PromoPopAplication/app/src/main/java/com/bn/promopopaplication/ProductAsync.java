package com.bn.promopopaplication;

import android.os.AsyncTask;

public class ProductAsync extends AsyncTask<Void, Void, Void>{


    @Override
    protected Void doInBackground(Void... params) {
        // Método que busca produtos assíncronamente.
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Método que envia os produtos encontrados para a main thread.
    }

}
