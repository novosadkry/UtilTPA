package cz.novosadkry.UtilTPA.Services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServiceProvider implements IServiceProvider {
    private final Map<Class<?>, IService> serviceMap;

    public ServiceProvider() {
        serviceMap = new HashMap<>();
    }

    public ServiceProvider(IService... services) {
        this(false, services);
    }

    public ServiceProvider(boolean initialize, IService... services) {
        serviceMap = new HashMap<>();

        for (IService service : services) {
            if (initialize)
                service.initialize();

            serviceMap.put(service.getClass(), service);
        }
    }

    @Override
    public void add(IService service) {
        add(service, false);
    }

    @Override
    public void add(IService service, boolean initialize) {
        if (initialize)
            service.initialize();

        IService prev = serviceMap.put(service.getClass(), service);

        if (prev != null)
            prev.terminate();
    }

    @Override
    public <T extends IService> T remove(Class<T> clazz) {
        return remove(clazz, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IService> T remove(Class<T> clazz, boolean terminate) {
        IService service = removeServiceOf(clazz);

        if (service != null && terminate) {
            service.terminate();
            return (T) service;
        }

        return null;
    }

    @Override
    public void removeAll() {
        removeAll(false);
    }

    @Override
    public void removeAll(boolean terminate) {
        for (Class<?> clazz : serviceMap.keySet()) {
            IService service = serviceMap.remove(clazz);

            if (terminate)
                service.terminate();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IService> T get(Class<T> clazz) {
        IService service = getServiceOf(clazz);
        return (T) service;
    }

    private IService removeServiceOf(Class<?> clazz) {
        IService service = serviceMap.remove(clazz);

        if (service != null)
            return service;

        Iterator<IService> it = serviceMap.values().iterator();

        while (it.hasNext()) {
            service = it.next();

            if (clazz.isInstance(service)) {
                it.remove();
                return service;
            }
        }

        return null;
    }

    private IService getServiceOf(Class<?> clazz) {
        IService service = serviceMap.get(clazz);

        if (service != null)
            return service;

        for (IService s : serviceMap.values()) {
            if (clazz.isInstance(s))
                return s;
        }

        return null;
    }
}
