/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for DB table Region
 * @author Philippe Lüthi, Elia Kocher
 */
public class Region {
    
    private int id;
    private String name;
    private int roomId;
    private int major;
    private List<Beacon> beacons = new ArrayList<>();
    
    public Region(int id, String name, int room, int major){
        this.id = id;
        this.name = name;
        this.roomId = room;
        this.major = major;
    }

    /**
     * adds a beacon to this region
     * @param b beacon to add
     */
    public void addBeacon(Beacon b){
        beacons.add(b);
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
     * @return the roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * @return the major
     */
    public int getMajor() {
        return major;
    }
    
    
    
}
