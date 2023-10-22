//A node containing the key-value pair
public class HashNode<K, V>{
    K key;
    V value;
    final int keyHashCode;
    //The next node int the chain
    HashNode<K,V> nextNode;

    public HashNode(K key, V value, int keyHashCode) {
        this.key = key;
        this.value = value;
        this.keyHashCode = keyHashCode;
    }
}
