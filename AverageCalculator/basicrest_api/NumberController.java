package com.basicrest_api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/numbers")
public class NumberController {

    @Autowired
    private NumberService numberService;

    @GetMapping("/{numberid}")
    public ResponseEntity<Map<String, Object>> getNumbers(@PathVariable String numberid) {
        Map<String, Object> response = numberService.getNumbers(numberid);
        return ResponseEntity.ok(response);
    }
}
