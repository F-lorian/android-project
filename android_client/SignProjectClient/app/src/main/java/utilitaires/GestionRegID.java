package utilitaires;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Axel_2 on 18/01/2016.
 */
public class GestionRegID {

    private Context context;

    public GestionRegID(Context c){
        this.context = c;
    }

    public String register() throws IOException {
        InstanceID instanceID = InstanceID.getInstance(this.context);
        String token = instanceID.getToken(Config.API_KEY,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        return token;
    }

    public void unregister() throws IOException {
        InstanceID instanceID = InstanceID.getInstance(this.context);
        instanceID.deleteToken(Config.API_KEY,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE);
    }
}
