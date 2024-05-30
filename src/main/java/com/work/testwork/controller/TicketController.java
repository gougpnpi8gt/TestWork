package com.work.testwork.controller;

import com.work.testwork.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TicketController {
    private TicketService ticketService;
    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/minFlightTimes")
    public Map<String, Long> getMinFlightTimes(@RequestParam String departureCity, @RequestParam String arrivalCity) {
        return ticketService.getMinFlightTimes(departureCity, arrivalCity);
    }

    @GetMapping("/priceStats")
    public Map<String, Double> getPriceStats(@RequestParam String departureCity, @RequestParam String arrivalCity) {
        return ticketService.getPriceStats(departureCity, arrivalCity);
    }
}
