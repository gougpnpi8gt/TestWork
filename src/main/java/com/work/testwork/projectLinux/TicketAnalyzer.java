package com.work.testwork.projectLinux;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketAnalyzer {
    // Запуск из командой строки: java TicketAnalyzer path/to/tickets.json
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Использование: java TicketAnalyzer <path-to-tickets.json>");
            return;
        }
        String filePath = args[0];
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JSONArray tickets = new JSONArray(content);
        Map<String, Long> minFlightTimes = new HashMap<>();
        List<Integer> prices = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticket = tickets.getJSONObject(i);
            String departureCity = ticket.getString("departure_city");
            String arrivalCity = ticket.getString("arrival_city");
            String carrier = ticket.getString("carrier");
            int price = ticket.getInt("price");
            if (departureCity.equals("Владивосток") && arrivalCity.equals("Тель-Авив")) {
                prices.add(price);
                LocalDateTime departureTime = LocalDateTime.parse(ticket.getString("departure_time"), formatter);
                LocalDateTime arrivalTime = LocalDateTime.parse(ticket.getString("arrival_time"), formatter);
                long flightTime = Duration.between(departureTime, arrivalTime).toMinutes();
                minFlightTimes.merge(carrier, flightTime, Math::min);
            }
        }
        System.out.println("Минимальное время полета для каждого перевозчика от Владивосток до Тель-Авив:");
        for (Map.Entry<String, Long> entry : minFlightTimes.entrySet()) {
            System.out.println("Перевозчик: " + entry.getKey() + ", Минимальное время полета: " + entry.getValue() + " минут");
        }
        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);
        Collections.sort(prices);
        double medianPrice;
        int size = prices.size();
        if (size % 2 == 0) {
            medianPrice = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            medianPrice = prices.get(size / 2);
        }
        System.out.println("\nСредняя цена: " + averagePrice);
        System.out.println("Медианая цена: " + medianPrice);
        System.out.println("Разница между средней и медианой ценой: " + Math.abs(averagePrice - medianPrice));
    }
}
