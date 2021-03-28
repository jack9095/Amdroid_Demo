// IFlyAidl.aidl
package com.kuanquan.binder_service;

// Declare any non-default types here with import statements
import com.kuanquan.binder_service.Person;

interface IFlyAidl {
    void addPerson(in Person person);
    List<Person> getPersonList();
}