/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Class for DB table Role
 * @author Philippe Lüthi, Elia Kocher
 */
public class Role {
    
    private int id;
    private String name;
    
    public Role(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
