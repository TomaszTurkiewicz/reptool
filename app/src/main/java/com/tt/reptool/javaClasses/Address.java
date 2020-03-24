package com.tt.reptool.javaClasses;


/*

Address:

 - name - client name
 - street with number
 - post code

 */

public class Address {
    private String name;
    private String street;
    private String postCode;

    public Address() {
    }

    public Address(String name, String street, String postCode) {
        this.name = name;
        this.street = street;
        this.postCode = postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {

            this.name = name1;

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street1) {

        this.street = street1;

    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode1) {

        this.postCode = postCode1.toUpperCase();

    }

    public String fullAddress(){
        return postCode+" "+street;
    }
}
