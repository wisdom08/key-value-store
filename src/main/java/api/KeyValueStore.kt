package kvstore.api

interface KeyValueStore<K, V> {

    fun put(key: K, value: V)

    fun get(key: K): V?

    fun delete(key: K)
}
