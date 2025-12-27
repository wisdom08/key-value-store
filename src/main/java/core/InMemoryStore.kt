package kvstore.core

import kvstore.api.KeyValueStore

class InMemoryStore<K, V> : KeyValueStore<K, V> {

    private val store = HashMap<K, V>()

    override fun put(key: K, value: V) {
        store[key] = value
    }

    override fun get(key: K): V? {
        return store[key]
    }

    override fun delete(key: K) {
        store.remove(key)
    }
}
