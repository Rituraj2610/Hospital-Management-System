package com.capgemini.hospital_management_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "On_Call")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @MapsId("nurseId")
    @ManyToOne
    @JoinColumn(name = "Nurse")
    @JsonBackReference
    private Nurse nurse;

    @MapsId("blockId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor"),
            @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode")
    })
    @JsonBackReference
    private Block block;

}
