package core

import kvstore.core.InMemoryStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class InMemoryStoreConcurrencyTest {

    @Test
    @DisplayName("멀티스레드 환경에서 HashMap 기반 저장소는 데이터 유실이 발생할 수 있다")
    fun concurrentPutTest() {
        val store = InMemoryStore<Int, Int>()

        val threadCount = 20
        val perThreadOperations = 10_000

        val executor = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        repeat(threadCount) { threadIndex ->
            executor.submit {
                val base = threadIndex * perThreadOperations
                for (i in 0 until perThreadOperations) {
                    store.put(base + i, i)
                }
                latch.countDown()
            }
        }

        latch.await()
        executor.shutdown()

        val expectedSize = threadCount * perThreadOperations
        var actualSize = 0

        for (i in 0 until expectedSize) {
            if (store.get(i) != null) {
                actualSize++
            }
        }

        // 이 assertion은 실패할 수 있음
        assertEquals(expectedSize, actualSize)
    }
}
