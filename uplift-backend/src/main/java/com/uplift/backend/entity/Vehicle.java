package com.uplift.backend.entity;

import com.uplift.backend.enums.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User rider;

    @Column(nullable = false, unique = true)
    private String plateNumber;

    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private boolean approved;
    private boolean active = true;

    private String licenseDoc;
    private String registrationDoc;

    // getters/setters
}
