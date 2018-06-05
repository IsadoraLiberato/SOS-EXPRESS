package sosexpress.com.br.sosexpres.service_firebase;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import sosexpress.com.br.sosexpres.utils.Constantes;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        Constantes.currentToken = refreshToken;

    }
}
