package com.tt.reptool.javaClasses;

/*

Manager or other person from company
 - name
 - surname
 - email address

Has to be part of the job (always)

 */

public class Manager {

    private String name;
    private String surname;
    private String emailAddress;

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    private boolean working;

    public Manager() {
        working=true;
    }

    public Manager(String name, String surname, String emailAddress){
        this.name=name;
        this.surname=surname;
        this.emailAddress=emailAddress;
    }

    public String getName() {
        if(name!=null){
        return name;
        }
        return null;
    }

    public void setName(String name) {
        if(name.isEmpty())
        {
            this.name = "";
        }
        else if(name.length() == 1)
        {
            this.name = name.toUpperCase();
        }
        else
            {
        this.name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
        }
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (surname.isEmpty()) {
            this.surname = "";
        } else if (surname.length() == 1) {
            this.surname = surname.toUpperCase();
        } else {

            this.surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String nameAndSurnameToString(){
        return name + " " + surname;
    }
}
