// src/main/java/com/harness/workshop/controller/UiController.java
package com.harness.workshop.controller;

import com.harness.workshop.model.User;
import com.harness.workshop.service.FlagService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UiController {
    private final FlagService service;
    public UiController(FlagService service) { this.service = service; }

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("users", service.getUsers());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(required = false) String userId,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String plan,
                          @RequestParam(required = false) String country,
                          HttpSession session) {
        User u;
        if (userId != null && !userId.isBlank() && service.getUsers().containsKey(userId)) {
            u = service.getUsers().get(userId);
        } else {
            String id = "adhoc-" + System.currentTimeMillis();
            u = new User(id,
                    name == null ? "Guest" : name,
                    email == null ? "" : email,
                    plan == null ? "free" : plan,
                    country == null ? "UK" : country,
                    Map.of());
            service.putUser(u);
        }
        session.setAttribute("uid", u.getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String uid = (String) session.getAttribute("uid");
        if (uid == null) return "redirect:/";
        User user = service.findUser(uid).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("users", service.getUsers());
        model.addAttribute("splits", service.listSplitNames());
        model.addAttribute("results", service.evalAll(user));
        return "dashboard";
    }

    @PostMapping("/impersonate")
    public String impersonate(@RequestParam String userId, HttpSession session) {
        if (service.getUsers().containsKey(userId)) session.setAttribute("uid", userId);
        return "redirect:/dashboard";
    }

    // NEW: Pretty Users page
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", service.getUsers());
        return "users";
    }

    // NEW: Pretty Splits page
    @GetMapping("/splits")
    public String splits(Model model, HttpSession session) {
        String uid = (String) session.getAttribute("uid");
        User user = uid == null ? null : service.findUser(uid).orElse(null);
        model.addAttribute("currentUser", user);
        model.addAttribute("splits", service.listSplitNames());
        return "splits";
    }

    // NEW: Pretty Evaluate page (HTML companion to /api/evaluate/{userId})
    @GetMapping("/evaluate")
    public String evaluate(Model model, HttpSession session) {
        String uid = (String) session.getAttribute("uid");
        if (uid == null) return "redirect:/";
        User user = service.findUser(uid).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("results", service.evalAll(user)); // Map<String, TreatmentView>
        return "evaluate";
    }
}
