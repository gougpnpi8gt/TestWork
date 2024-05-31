package com.work.testwork.projectLinux;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TicketAnalyzer {
    // Запуск из командой строки: java TicketAnalyzer path/to/tickets.json
    public static void main(String[] args) {
//        Минимальное время полета для каждого авиаперевозчика:
//        TK: 555 минут
//        S7: 605 минут
//        SU: 480 минут
//        BA: 485 минут
//        Разница между средней ценой и медианой: -283.33333333333394

        String filePath = "D:\\Projects\\TestWork\\src\\main\\java\\com\\work\\testwork\\js\\tickets.json";
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray tickets = jsonObject.getJSONArray("tickets");

            // Рассчитать минимальное время полета для каждого авиаперевозчика
            Map<String, Long> minFlightTimes = calculateMinFlightTimes(tickets);
            System.out.println("Минимальное время полета для каждого авиаперевозчика:");
            for (Map.Entry<String, Long> entry : minFlightTimes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " минут");
            }

            // Рассчитать разницу между средней ценой и медианой
            double priceDifference = calculatePriceDifference(tickets);
            System.out.println("Разница между средней ценой и медианой: " + priceDifference);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Long> calculateMinFlightTimes(JSONArray tickets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        Map<String, Long> minFlightTimes = new HashMap<>();

        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticket = tickets.getJSONObject(i);
            if (ticket.getString("origin_name").equals("Владивосток") &&
                    ticket.getString("destination_name").equals("Тель-Авив")) {
                String carrier = ticket.getString("carrier");
                LocalDateTime departure = LocalDateTime.parse(ticket.getString("departure_date") + " " + ticket.getString("departure_time"), formatter);
                LocalDateTime arrival = LocalDateTime.parse(ticket.getString("arrival_date") + " " + ticket.getString("arrival_time"), formatter);
                long flightDuration = Duration.between(departure, arrival).toMinutes();

                minFlightTimes.put(carrier, Math.min(minFlightTimes.getOrDefault(carrier, Long.MAX_VALUE), flightDuration));
            }
        }
        return minFlightTimes;
    }

    private static double calculatePriceDifference(JSONArray tickets) {
        List<Integer> prices = new ArrayList<>();

        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticket = tickets.getJSONObject(i);
            if (ticket.getString("origin_name").equals("Владивосток") &&
                    ticket.getString("destination_name").equals("Тель-Авив")) {
                prices.add(ticket.getInt("price"));
            }
        }

        if (prices.isEmpty()) return 0;

        double average = prices.stream().mapToInt(Integer::intValue).average().orElse(0);

        Collections.sort(prices);
        double median;
        int size = prices.size();
        if (size % 2 == 0) {
            median = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            median = prices.get(size / 2);
        }

        return average - median;
    }
}
