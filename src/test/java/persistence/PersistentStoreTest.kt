package kvstore.persistence

import kvstore.core.InMemoryStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class PersistentStoreTest {

    @Test
    fun `재시작 후 로그를 통해 데이터가 복구된다`() {
        val logFile = File("build/test-log.txt")
        logFile.delete()

        // 1. 첫 실행
        run {
            val store = PersistentStore(
                delegate = InMemoryStore(),
                log = AppendOnlyLog(logFile)
            )

            store.put("a", "1")
            store.put("b", "2")
            store.delete("a")
        }

        // 2. 재시작 (새 인스턴스)
        val recoveredStore = PersistentStore(
            delegate = InMemoryStore(),
            log = AppendOnlyLog(logFile)
        )

        assertEquals(null, recoveredStore.get("a"))
        assertEquals("2", recoveredStore.get("b"))
    }
}
