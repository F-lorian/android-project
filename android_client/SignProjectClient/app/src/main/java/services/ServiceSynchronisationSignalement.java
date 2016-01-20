package services;

import android.app.IntentService;
import android.content.Intent;

import modeles.modele.Signalement;
import modeles.modeleBD.SignalementBD;
import utilitaires.SessionManager;

// TODO : Faire une service de synchronisation des données local vers le serveur si détection de connexion.

/**
 * Created by Axel_2 on 24/12/2015.
 */
public class ServiceSynchronisationSignalement extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServiceSynchronisationSignalement(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SessionManager sessionManager = new SessionManager(this);
        while (sessionManager.isLoggedIn())
        {
            try {
                Thread.sleep(10000);


                if (this.nbSignalementsLocal() > 0)
                {
                    SignalementBD signalementBD = new SignalementBD(this);
                    signalementBD.open();
                    Signalement s = signalementBD.getFirstSignalement(SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER);
                    signalementBD.close();

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private long nbSignalementsLocal()
    {
        SignalementBD s = new SignalementBD(this);
        s.open();
        long nb = s.getCount(SignalementBD.TABLE_NAME_SIGNALEMENT_A_ENVOYER);
        s.close();

        return nb;
    }
}
