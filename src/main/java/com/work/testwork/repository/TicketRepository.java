package com.work.testwork.repository;

import com.work.testwork.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity);

}
