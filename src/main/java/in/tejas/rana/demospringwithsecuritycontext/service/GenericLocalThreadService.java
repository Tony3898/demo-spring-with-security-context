package in.tejas.rana.demospringwithsecuritycontext.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GenericLocalThreadService {

    private final ThreadLocal<Map<String, String>> currentTenant;

    GenericLocalThreadService() {
        this.currentTenant = ThreadLocal.withInitial(HashMap::new);
    }

    public String get(String dataKey) {
        if (null == currentTenant.get())
            currentTenant.set(new HashMap<>());
        return currentTenant.get().get(dataKey);
    }

    public void set(String key, String value) {
        currentTenant.get().put(key, value);
    }

    public void clear() {
        currentTenant.remove();
    }
}
