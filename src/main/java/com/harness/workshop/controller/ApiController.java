package com.harness.workshop.controller;


import com.harness.workshop.model.*;
import com.harness.workshop.service.FlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ApiController {
    private final FlagService service;
    public ApiController(FlagService service) { this.service = service; }

    @GetMapping("/users")
    public Map<String, ?> users() {
        System.out.println("API Call: Users");
        return service.getUsers(); }

    @GetMapping("/splits")
    public Object splits() { return service.listSplitNames(); }

    @GetMapping("/evaluate/{userId}")
    public ResponseEntity<?> evalAll(@PathVariable String userId) {
        System.out.println("Evaluating user: "+userId);
        return service.findUser(userId)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(service.evalAll(u)))
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error","USER_NOT_FOUND")));
    }

    @PostMapping("/evaluate/{split}/{userId}")
    public ResponseEntity<?> evalOne(@PathVariable String split, @PathVariable String userId) {
        System.out.println("Evaluating user: "+userId);
        return service.findUser(userId)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(service.eval(split, u)))
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error","USER_NOT_FOUND")));
    }
}
