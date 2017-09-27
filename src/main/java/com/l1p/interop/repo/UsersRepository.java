package com.l1p.interop.repo;

public class UsersRepository {

    private String fileLocation;

    public UsersRepository() {
        this.fileLocation = System.getProperty("user.home")+"/users_db.csv";;
    }

    public String findUserById(String id){

        return null;
    }
}
