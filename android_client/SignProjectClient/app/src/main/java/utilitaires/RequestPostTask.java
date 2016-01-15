package utilitaires;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.florian.signprojectclient.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Florian on 09/01/2016.
 */
public class RequestPostTask extends AsyncTask<Void,Void,Void> {

    private PostRequest postRequest;
    private ProgressDialog progressDialog;
    private Activity activity;
    private String result;
    Handler mHandler;
    private final String messageDialog;

    public RequestPostTask(String action, List pairs,Handler mHandler,  Activity activity, String messageDialog){
        this.postRequest = new PostRequest(action,pairs);
        this.activity = activity;
        this.mHandler = mHandler;
        this.messageDialog = messageDialog;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.postRequest.sendRequest();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.progressDialog = ProgressDialog.show(activity, activity.getResources().getString(R.string.progress_dialog_titre), this.messageDialog);

        this.progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.result = this.postRequest.getResultat();
        progressDialog.dismiss();

        System.out.println("réponse : "+this.result);
        Message msg=new Message();

        msg.obj=this.result;

        mHandler.sendMessage(msg);
    }

    public static void sendRequest(String command,Map<String, String> params, Handler handler, Activity activity, String messageDialog){

        List<NameValuePair> pairsPost = getPairsPost(params);
        RequestPostTask requestPostTask = new RequestPostTask(command, pairsPost, handler, activity, messageDialog);
        requestPostTask.execute();
    }

    public static List<NameValuePair> getPairsPost(Map<String, String> params){

        List<NameValuePair> pairsPost = new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry : params.entrySet()){
            String key = entry.getKey();
            String val = entry.getValue();
            if(val != null){
                pairsPost.add(new BasicNameValuePair(key, val));
            }
        }

        return pairsPost;
    }

}