package com.tt.reptool;

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

    public Manager() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
