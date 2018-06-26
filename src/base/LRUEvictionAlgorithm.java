package base;

import java.time.LocalTime;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LRUEvictionAlgorithm {

    private static AtomicInteger timer = new AtomicInteger(0);

    public static class CacheEntry implements Comparable{
        private int key;

        private int value;

        private int time = timer.incrementAndGet();

        public CacheEntry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return Key.
         */
        public int key() {
            return key;
        }

        /**
         * @param key New key.
         */
        public void key(int key) {
            this.key = key;
        }

        /**
         * @return Value.
         */
        public int value() {
            return value;
        }

        /**
         * @param val New value.
         */
        public void value(int val) {
            if (this.value != val)
                time = timer.incrementAndGet();

            value = val;
        }

        /**
         *
         */
        private void onEvicted(){
            System.out.println("Evicting entry[key=" + key + ", " + value + "]");
        }

        /** {@inheritDoc} */
        @Override public int compareTo(Object o) {
            return this.time - ((CacheEntry)o).time;
        }
    }

    public static class LRUEvictionCache extends PriorityBlockingQueue{
        private PriorityQueue<CacheEntry> queue = new PriorityQueue<>(5);

        public void put(int key, int value){
            CacheEntry entry = queue.stream().filter(entry0 -> entry0.key() == key).findFirst().orElse(null);

            if (entry != null) {
                queue.remove(entry);

                entry.value(value);

                queue.add(entry);

                return;
            }

            entry = new CacheEntry(key, value);

            if (queue.size() >= 5) {
                CacheEntry entry0 = queue.remove();

                entry0.onEvicted();
            }

            queue.add(entry);
        }

        /**
         * @param key Key.
         */
        public CacheEntry get(int key){
            Object[] res = queue.stream().filter(entry -> entry.key() == key).toArray();

            assert res.length <= 1;

            return (CacheEntry)res[0];
        }
    }

    public static void main(String[] args) {
        LRUEvictionCache cache = new LRUEvictionCache();

        cache.put(1, 1);
        cache.put(2, 1);//1
        cache.put(3, 1);
        cache.put(4, 1);//2
        cache.put(5, 1);//3

        cache.put(1, 2);
        cache.put(3, 2);// 2, 4, 5

        //----------------------

        cache.put(6, 1);
        cache.put(7, 1);
        cache.put(8, 1);
    }
}
