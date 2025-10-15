package com.harness.workshop.service;
import com.harness.workshop.model.TreatmentView;
import com.harness.workshop.model.User;
import io.split.client.SplitClient;
import io.split.client.SplitManager;
import io.split.client.api.Key;
import io.split.client.api.SplitView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlagService {
    private final Map<String, User> users = new LinkedHashMap<>();
    private final SplitClient splitClient;
    private final SplitManager splitManager;
    private final List<String> configuredSplits;

    public FlagService(SplitClient splitClient,
                       SplitManager splitManager,
                       @Value("${app.splits:}") List<String> splits) {
        this.splitClient = splitClient;
        this.splitManager = splitManager;
        this.configuredSplits = (splits == null) ? List.of() : splits;
        System.out.println("Flag Service constructor displays list of splits: " +configuredSplits);
    }

    public Map<String, User> getUsers() { return users; }
    public void putUser(User u) { users.put(u.getId(), u); }
    public Optional<User> findUser(String id) { return Optional.ofNullable(users.get(id)); }

    public List<String> listSplitNames() {
        if (configuredSplits != null && !configuredSplits.isEmpty()) return configuredSplits;
        System.out.println("Split Manager generating split names");
        System.out.println(splitManager.splitNames().toString());

        //Use with direct sdk -> split approach
        return new ArrayList<>(splitManager.splitNames());



        //Use with synchroniser
//        List<String> result = splitManager.splitNames().stream()
//                .map(s -> s.replaceFirst("^SPLITIOsplit", "")) // remove prefix if present
//                .collect(Collectors.toList());
//
//        System.out.println(result);

//        return result; // returns List<String>
    }

    public TreatmentView eval(String split, User user) {
        System.out.println("Treatment evaluation called");
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("plan", user.getPlan());
        attrs.put("country", user.getCountry());
        if (user.getAttributes() != null) attrs.putAll(user.getAttributes());

        // New SDKs use String keys directly, not Key objects
        String key = user.getId();

        System.out.println("Getting treatment for split: "+split+" and user: "+key);
        // Use getTreatment (config/label no longer supported)
        String treatment = splitClient.getTreatment(key, split, attrs);
        System.out.print(treatment);

        // These fields aren’t exposed anymore, so leave null for compatibility
        String configJson = null;
        String label = null;

        return new TreatmentView(split, treatment, configJson, label);
    }

    public Map<String, TreatmentView> evalAll(User user) {
        System.out.println("Calling evalAll() with user: "+user.getId());
        LinkedHashMap<String, TreatmentView> out = new LinkedHashMap<>();
        for (String s : listSplitNames()) {
            out.put(s, eval(s, user));
            System.out.println(eval(s,user));
        }

        return out;
    }
}
