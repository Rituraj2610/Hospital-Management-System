package com.capgemini.hospital_management_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "On_Call")
@IdClass(OnCallId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnCall {
    @Id
    @ManyToOne
    @JoinColumn(name = "Nurse")
    private Nurse nurse;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor"),
            @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode")
    })
    private Block block;

    @Id
    @Column(name = "OnCallStart")
    private LocalDateTime onCallStart;

    @Id
    @Column(name = "OnCallEnd")
    private LocalDateTime onCallEnd;

}
