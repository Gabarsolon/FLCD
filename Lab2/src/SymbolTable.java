import java.util.ArrayList;
import java.util.Objects;

public class SymbolTable<K, V> {
    private final double THRESHOLD = 0.75;
    private ArrayList<HashNode<K, V>> bucketArray;
    private int numBuckets;
    private int size;

    public SymbolTable() {
        bucketArray = new ArrayList<>();
        numBuckets = 10;
        size = 0;

        for (int i = 0; i < numBuckets; i++)
            bucketArray.add(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int hashCode(K key) {
        return Objects.hashCode(key);
    }

    private int getBucketIndex(K key) {
        int hashCode = hashCode(key);
        int index = hashCode % numBuckets;
        if (index < 0)
            index = index * -1;
        return index;
    }

    private void resize() {
        ArrayList<HashNode<K, V>> oldBucketArray = bucketArray;
        bucketArray = new ArrayList<>();
        numBuckets = 2 * numBuckets;
        size = 0;
        for (int i = 0; i < numBuckets; i++)
            bucketArray.add(null);
        for (HashNode<K, V> headNode : oldBucketArray) {
            while (headNode != null) {
                add(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }

    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);

        HashNode<K, V> head = bucketArray.get(bucketIndex);
        while (head != null) {
            if (head.key.equals(key) && head.hashCode == hashCode)
                return head.value;
            head = head.next;
        }
        return null;
    }

    public void add(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);
        HashNode<K, V> head = bucketArray.get(bucketIndex);
        while (head != null) {
            if (head.key.equals(key) && head.hashCode == hashCode) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        size++;
        head = bucketArray.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<K, V>(key, value, hashCode);
        newNode.next = head;
        bucketArray.set(bucketIndex, newNode);

        if ((double) size / numBuckets >= THRESHOLD)
            resize();
    }

    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);
        HashNode<K, V> head = bucketArray.get(bucketIndex);
        HashNode<K, V> prev = null;
        while (head != null) {
            if (head.key.equals(key) && hashCode == head.hashCode)
                break;
            prev = head;
            head = head.next;
        }
        if (head == null)
            return null;
        size--;
        if (prev == null)
            bucketArray.set(bucketIndex, head.next);
        else
            prev.next = head.next;
        return head.value;
    }
}