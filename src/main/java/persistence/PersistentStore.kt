package kvstore.persistence

import kvstore.api.KeyValueStore

class PersistentStore(
    private val delegate: KeyValueStore<String, String>,
    private val log: AppendOnlyLog
) : KeyValueStore<String, String> {

    init {
        // 재시작 시 로그 리플레이
        log.replay(
            onPut = { key, value -> delegate.put(key, value) },
            onDelete = { key -> delegate.delete(key) }
        )
    }

    override fun put(key: String, value: String) {
        log.appendPut(key, value)
        delegate.put(key, value)
    }

    override fun get(key: String): String? {
        return delegate.get(key)
    }

    override fun delete(key: String) {
        log.appendDelete(key)
        delegate.delete(key)
    }
}
