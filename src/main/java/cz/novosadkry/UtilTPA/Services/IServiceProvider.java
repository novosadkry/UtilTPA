package cz.novosadkry.UtilTPA.Services;

public interface IServiceProvider {
    void add(IService service, boolean initialize);

    <T> void remove(Class<T> clazz, boolean terminate);

    <T> T get(Class<T> clazz);
}
