package com.capgemini.hospital_management_system.model;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Block")
@IdClass(BlockId.class)
public class Block {
    @Id
    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Id
    @Column(name = "BlockCode")
    private Integer blockCode;

    @OneToMany(mappedBy = "block")
    private Set<Room> rooms = new HashSet<>();

    @OneToMany(mappedBy = "block")
    private Set<OnCall> onCalls = new HashSet<>();

    // Getters and setters
    public Integer getBlockFloor() {
        return blockFloor;
    }

    public void setBlockFloor(Integer blockFloor) {
        this.blockFloor = blockFloor;
    }

    public Integer getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(Integer blockCode) {
        this.blockCode = blockCode;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public Set<OnCall> getOnCalls() {
        return onCalls;
    }

    public void setOnCalls(Set<OnCall> onCalls) {
        this.onCalls = onCalls;
    }
}