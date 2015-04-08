/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Class for DB table Performance
 * @author Philippe Lüthi, Elia Kocher
 */
public class Beacon {    
    
    private int id;
    private String name;
    private String location;
    private int major;
    private int minor;
    private int roomId;
    
    public Beacon(int id, String name, String location, int major, int minor, int room){
        this.id = id;
        this.name = name;
        this.location = location;
        this.major = major;
        this.minor = minor;
        this.roomId = room;
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

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the major
     */
    public int getMajor() {
        return major;
    }

    /**
     * @return the minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * @return the roomId
     */
    public int getRoomId() {
        return roomId;
    }
    
}
