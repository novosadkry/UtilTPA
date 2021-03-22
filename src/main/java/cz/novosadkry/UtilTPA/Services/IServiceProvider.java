package cz.novosadkry.UtilTPA.Services;

public interface IServiceProvider {
    void add(IService service);
    void add(IService service, boolean initialize);

    <T extends IService> T remove(Class<T> clazz);
    <T extends IService> T remove(Class<T> clazz, boolean terminate);

    void removeAll();
    void removeAll(boolean terminate);

    <T extends IService> T get(Class<T> clazz);
}
