package com.capgemini.hospital_management_system.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Medication")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    @Id
    @Column(name = "Code")
    private Integer code;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Brand", nullable = false)
    private String brand;

    @Column(name = "Description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Prescribes> prescriptions = new HashSet<>();


}
