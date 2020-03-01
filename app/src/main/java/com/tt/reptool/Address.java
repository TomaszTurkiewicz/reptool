package com.tt.reptool;


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

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode.toUpperCase();
    }
}
