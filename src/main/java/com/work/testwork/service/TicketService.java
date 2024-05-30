package com.work.testwork.service;

import com.work.testwork.entity.Ticket;
import com.work.testwork.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository repository) {
        this.ticketRepository = repository;
    }

    public Map<String, Long> getMinFlightTimes(String departureCity, String arrivalCity) {
        List<Ticket> tickets = ticketRepository.findByDepartureCityAndArrivalCity(departureCity, arrivalCity);
        Map<String, Long> minFlightTimes = new HashMap<>();
        tickets.forEach(ticket -> {
            long flightTime = Duration.between(ticket.getDepartureTime(), ticket.getArrivalTime()).toMinutes();
            minFlightTimes.merge(ticket.getCarrier(), flightTime, Math::min);
        });

        return minFlightTimes;
    }

    public Map<String, Double> getPriceStats(String departureCity, String arrivalCity) {
        List<Ticket> tickets = ticketRepository.findByDepartureCityAndArrivalCity(departureCity, arrivalCity);
        List<Integer> prices = tickets.stream().map(Ticket::getPrice).collect(Collectors.toList());

        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);
        Collections.sort(prices);
        double medianPrice;
        int size = prices.size();
        if (size % 2 == 0) {
            medianPrice = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            medianPrice = prices.get(size / 2);
        }

        Map<String, Double> priceStats = new HashMap<>();
        priceStats.put("averagePrice", averagePrice);
        priceStats.put("medianPrice", medianPrice);
        priceStats.put("difference", Math.abs(averagePrice - medianPrice));

        return priceStats;
    }
}
