package com.work.testwork.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    @Id
    Long id;
    String carrier;
    String departureCity;
    String arrivalCity;
    LocalDateTime departureTime;
    LocalDateTime arrivalTime;
    int price;
}

