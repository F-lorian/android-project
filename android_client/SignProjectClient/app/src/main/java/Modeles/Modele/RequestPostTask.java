package modeles.modele;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.florian.signprojectclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import activites.AccueilUserActivity;
import utilitaires.Config;
import utilitaires.PostRequest;
import utilitaires.SessionManager;

/**
 * Created by Florian on 09/01/2016.
 */
public class RequestPostTask extends AsyncTask<Void,Void,Void> {

    private PostRequest postRequest;
    private ProgressDialog progressDialog;
    private Activity activity;
    private String result;
    Handler mHandler;

    public RequestPostTask(String action, List pairs,Handler mHandler,  Activity activity){
        this.postRequest = new PostRequest(action,pairs);
        this.activity = activity;
        this.mHandler = mHandler;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.postRequest.sendRequest();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.progressDialog = ProgressDialog.show(activity, activity.getResources().getString(R.string.progress_dialog_titre), activity.getResources().getString(R.string.progress_dialog_message_connection));

        this.progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        this.result = this.postRequest.getResultat();
        progressDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(this.result);
            Message msg=new Message();

            msg.obj=jsonObject;
            mHandler.sendMessage(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}