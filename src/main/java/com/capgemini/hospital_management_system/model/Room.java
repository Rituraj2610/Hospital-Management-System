package com.capgemini.hospital_management_system.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @Column(name = "RoomNumber")
    private Integer roomNumber;

    @Column(name = "RoomType", nullable = false)
    private String roomType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor"),
            @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode")
    })
    @JsonBackReference
    private Block block;

    @Column(name = "Unavailable", nullable = false)
    private Boolean unavailable;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Stay> stays = new HashSet<>();

}
