package utilitaires;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Axel_2 on 06/01/2016.
 */
public class PostRequest {

    private String url;
    private List pairs;
    private String resultat = "";

    public PostRequest(String action, List pairs){
        this.url = Config.SERVEUR_URL+"?action="+action;
        this.pairs = pairs;
        this.resultat = "";
    }

    public void sendRequest() {
        // Création du client http et du header

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.url);
        try {
            // Ajout des données à la requête
            httppost.setEntity(new UrlEncodedFormEntity(this.pairs));
            // Excécution de la requête
            HttpResponse response = httpclient.execute(httppost);
            // Lecture de la réponse
            if (response.getEntity().getContentLength() != 0) {
                StringBuilder sb = new StringBuilder();
                try {
                    InputStream is = response.getEntity().getContent();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    Log.i("Resultat : ", sb.toString());
                    this.resultat = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Resultat : ",
                        "Pas de contenu affiché dans le body de la réponse");
                this.resultat = "Pas de contenu affiché dans le body de la réponse";
            }
        } catch (ClientProtocolException e) {
            this.resultat = "Impossible d'effectuer la tâche";
        } catch (IOException e) {
            this.resultat = "Impossible d'effectuer la tâche";
        }

    }

    public String getResultat(){
        return this.resultat;
    }

}
