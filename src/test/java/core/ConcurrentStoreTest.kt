package core

import kvstore.core.ConcurrentStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class ConcurrentStoreConcurrencyTest {

    @Test
    @DisplayName("ConcurrentHashMap 기반 저장소는 멀티스레드 환경에서도 데이터 유실이 발생하지 않는다")
    fun concurrentPutTest() {
        val store = ConcurrentStore<Int, Int>()

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

        assertEquals(expectedSize, actualSize)
    }
}
