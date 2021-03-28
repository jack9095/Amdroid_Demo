package com.kuanquan.binder_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import com.kuanquan.binder_service.Person;

import java.util.ArrayList;
import java.util.List;

public class FlyAidlService extends Service {

    // 1
    private ArrayList<Person> personArrayList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        personArrayList = new ArrayList<>();
        return iBinder;
    }

    // 2
    private IBinder iBinder = new IFlyAidl.Stub(){

        @Override
        public void addPerson(Person person) throws RemoteException {
            personArrayList.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return personArrayList;
        }
    };
}
