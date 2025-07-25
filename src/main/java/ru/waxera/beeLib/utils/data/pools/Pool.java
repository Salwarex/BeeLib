package ru.waxera.beeLib.utils.data.pools;

/**
 * An interface designed to store objects of the same type in memory
 * and easily interact with them.
 *
 * <p>
 * It was originally created to interact with objects initialized based
 * on information from the {@link ru.waxera.beeLib.utils.data.database.Database Database}
 * to prevent the repeated creation of identical objects based on the same information.
 * </p>
 *
 * @param <K> It is the class or interface of the key element that should
 *           be used to search for the collection specified in the inherited
 *           class.
 * @param <T> It is a class or interface of objects stored in the pool.
 *
 * @version 1.0
 * @since 1.3.1
 * @author Salwarex
 */


public interface Pool<K, T> {
    /**
     * Pool addition method
     *
     * <p>
     * The method responsible for adding new objects to the pool. Depending on the
     * implementation, it may allow or prohibit key substitution if an element with
     * a similar key already exists.
     * </p>
     *
     * @param key A reference to an existing key object
     * @param element A reference to an existing object
     *                that should be in the pool
     */
    void add(K key, T element);
    boolean contains(K key);
    T get(K key);
    void remove(K key);
}
