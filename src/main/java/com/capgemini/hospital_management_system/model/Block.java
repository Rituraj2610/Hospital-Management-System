package com.capgemini.hospital_management_system.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Block")
@IdClass(BlockId.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Block {
    @Id
    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Id
    @Column(name = "BlockCode")
    private Integer blockCode;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Room> rooms = new HashSet<>();

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OnCall> onCalls = new HashSet<>();
}