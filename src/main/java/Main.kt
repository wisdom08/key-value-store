package kvstore

import kvstore.core.InMemoryStore

fun main() {
    val store = InMemoryStore<String, String>()

    store.put("a", "1")
    store.put("b", "2")

    println(store.get("a")) // 1

    store.delete("a")
    println(store.get("a")) // null
}
