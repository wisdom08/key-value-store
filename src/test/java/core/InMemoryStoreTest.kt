package core

import kvstore.core.InMemoryStore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InMemoryStoreTest {

    private lateinit var store: InMemoryStore<String, String>

    @BeforeEach
    fun setUp() {
        store = InMemoryStore()
    }

    @Test
    @DisplayName("키에 값을 저장하고 조회할 수 있다")
    fun putAndGet() {
        store.put("key1", "value1")

        val result = store.get("key1")

        assertEquals("value1", result)
    }

    @Test
    @DisplayName("존재하지 않는 키 조회 시 null 반환")
    fun getNonExistingKey() {
        val result = store.get("missing")

        assertNull(result)
    }

    @Test
    @DisplayName("기존 키에 값 재저장 시 덮어쓴다")
    fun overwriteValue() {
        store.put("key1", "value1")
        store.put("key1", "value2")

        val result = store.get("key1")

        assertEquals("value2", result)
    }

    @Test
    @DisplayName("키 삭제 후 조회하면 null 반환")
    fun deleteKey() {
        store.put("key1", "value1")

        store.delete("key1")

        assertNull(store.get("key1"))
    }
}
