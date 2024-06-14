package com.basicrest_api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NumberService {

    private static final int WINDOW_SIZE = 10;
    private static final Map<String, String> API_ENDPOINTS = Map.of(
            "p", "http://20.244.56.144/test/primes",
            "f", "http://20.244.56.144/test/fibonacci",
            "e", "http://20.244.56.144/test/even",
            "r", "http://20.244.56.144/test/random"
    );

    private final List<Integer> storedNumbers = new CopyOnWriteArrayList<>();

    public Map<String, Object> getNumbers(String numberid) {
        if (!API_ENDPOINTS.containsKey(numberid)) {
            throw new IllegalArgumentException("Invalid number ID");
        }

        List<Integer> windowPrevState = new ArrayList<>(storedNumbers);
        List<Integer> numbers = fetchNumbers(API_ENDPOINTS.get(numberid));

        // Maintain unique numbers
        for (Integer num : numbers) {
            if (!storedNumbers.contains(num)) {
                storedNumbers.add(num);
                if (storedNumbers.size() > WINDOW_SIZE) {
                    storedNumbers.remove(0);
                }
            }
        }

        double avg = storedNumbers.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        Map<String, Object> response = new HashMap<>();
        response.put("windowPrevState", windowPrevState);
        response.put("windowCurrState", new ArrayList<>(storedNumbers));
        response.put("numbers", numbers);
        response.put("avg", avg);

        return response;
    }

    private List<Integer> fetchNumbers(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return (List<Integer>) response.get("numbers");
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
