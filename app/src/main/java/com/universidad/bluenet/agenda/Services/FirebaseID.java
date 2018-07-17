package com.universidad.bluenet.agenda.Services;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Pc on 27/4/2018.
 */

public class FirebaseID extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
