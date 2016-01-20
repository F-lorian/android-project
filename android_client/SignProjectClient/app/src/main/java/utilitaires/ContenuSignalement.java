package utilitaires;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Axel_2 on 20/01/2016.
 */
public class ContenuSignalement {

    protected String ligneArret;
    protected int idLigneArret;
    protected List<String> tempsAttente;

    public ContenuSignalement(String ligneArret, int idLigneArret, List<String> tempsAttente)
    {
        this.ligneArret = ligneArret;
        this.idLigneArret = idLigneArret;
        this.tempsAttente = tempsAttente;
    }

    public ContenuSignalement(String contenu)
    {
        try {
            JSONObject contenuJson = new JSONObject(contenu);
            this.ligneArret = contenuJson.getString("ligneArret");
            this.idLigneArret = contenuJson.getInt("idLigneArret");

            JSONArray jsonArray = contenuJson.getJSONArray("horaires");
            this.tempsAttente = new ArrayList<>();
            for (int i=0; i<jsonArray.length(); i++)
            {
                this.tempsAttente.add(jsonArray.getJSONObject(i).getString("horaire"));
            }

            System.out.println(this.tempsAttente);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJson()
    {
        try {
            JSONObject jsonPrincipal = new JSONObject();

            jsonPrincipal.put("ligneArret", this.ligneArret);
            jsonPrincipal.put("idLigneArret", this.idLigneArret);

            JSONArray jsonArrayHoraires = new JSONArray();
            for (int i = 0; i < this.tempsAttente.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("horaire", this.tempsAttente.get(i));
                jsonArrayHoraires.put(jsonObject);
            }

            jsonPrincipal.put("horaires",jsonArrayHoraires);
            return jsonPrincipal;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getIdLigneArret() {
        return idLigneArret;
    }

    public String getLigneArret() {
        return ligneArret;
    }

    public List<String> getTempsAttente() {
        return tempsAttente;
    }

    public void setIdLigneArret(int idLigneArret) {
        this.idLigneArret = idLigneArret;
    }

    public void setLigneArret(String ligneArret) {
        this.ligneArret = ligneArret;
    }

    public void setTempsAttente(List<String> tempsAttente) {
        this.tempsAttente = tempsAttente;
    }
}
