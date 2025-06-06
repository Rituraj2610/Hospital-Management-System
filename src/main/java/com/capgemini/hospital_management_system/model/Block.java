package com.capgemini.hospital_management_system.model;
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

    @OneToMany(mappedBy = "block")
    private Set<Room> rooms = new HashSet<>();

    @OneToMany(mappedBy = "block")
    private Set<OnCall> onCalls = new HashSet<>();
}