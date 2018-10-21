package com.example.senamit.stationaryhutpro.liveData;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private static final String TAG = FirebaseQueryLiveData.class.getSimpleName();

    private Query query;
    private MyValueEventListener listener = new MyValueEventListener();

    private boolean listenerRemovePending = false;
    private Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;

        }
    };

    public FirebaseQueryLiveData(Query query) {
        Log.i(TAG, "inside the constructor query "+query);
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference ref) {
        Log.i(TAG, "inside constructor "+ref);
        this.query = ref;
    }


    @Override
    protected void onActive() {
        super.onActive();
        if (listenerRemovePending){
            handler.removeCallbacks(removeListener);
        }else {
            query.addValueEventListener(listener);
        }
        listenerRemovePending= false;

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending=true;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.w(TAG, "inside the firebaseQueryLiveData onDataChange");
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG, "cant listen to query, live data class error");
        }
    }

}
