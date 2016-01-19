package services;

import android.app.IntentService;
import android.content.Intent;

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

    }
}
