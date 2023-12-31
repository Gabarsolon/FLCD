import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
//for documentation:
//reg exp
//data structure for pif
//class diagram with all classes
//1a -> One instance for st boss

public class SymbolTable<T>{
    //the indicator which tells when to resize the bucket array
    private final double THRESHOLD = 0.75;
    //The array containing the buckets
    private ArrayList<HashNode<T, Integer>> bucketArray;
    //The size of the bucketArray
    private int numberOfBuckets;
    //The actually number of stored elements
    private int numberOfElements;



    public SymbolTable() {
        bucketArray = new ArrayList<>();
        //the number of buckets will be a prime number in order to have a more uniformly distribution
        numberOfBuckets = 7;
        //initially we don't have any elements
        numberOfElements = 0;
        //initialize the bucker array by adding null nodes
        for (int i = 0; i < numberOfBuckets; i++)
            bucketArray.add(null);
    }

    //O(1)
    public int size() {
        return numberOfElements;
    }

    //O(1)
    public boolean isEmpty() {
        return numberOfElements == 0;
    }

    //O(1)
    private int hashCode(T key) {
        //generate a hash code for the given key using the built-in Java function
        return Objects.hashCode(key);
    }

    //O(1)
    private int getBucketIndex(T key) {
        int hashCode = hashCode(key);
        //find the bucket for the key coresponding to its hash code
        int index = hashCode % numberOfBuckets;
        //the hash code might be negative
        if (index < 0) index = index * -1;
        return index;
    }

    //O(1)
    private boolean elementIsEqualToNode(HashNode<T, Integer> node, T key, int keyHashCode) {
        //check if a key is equal to a node and if it has the same hash code with it
        return node.key.equals(key) && node.keyHashCode == keyHashCode;
    }

    //alpha = numberOfElements/numberOfBuckets
    //O(numberOfElements + 1/(1-alpha))
    private void resize() {
        //save a copy of
        ArrayList<HashNode<T, Integer>> oldBucketArray = new ArrayList<>(bucketArray);

        //empty the current bucket array
        bucketArray = new ArrayList<>();
        //change the number of buckets to a prime number greater than the current value
        numberOfBuckets = Prime.nextPrimeAfter(numberOfBuckets);
        //reinitialize the bucket array
        for (int i = 0; i < numberOfBuckets; i++)
            bucketArray.add(null);

        //add again each element to the bucket array, each of them having maybe a new bucket assigned
        for (HashNode<T, Integer> currentNode : oldBucketArray) {
            while(currentNode != null){
                int bucketIndex = getBucketIndex(currentNode.key);
                int hashCode = hashCode(currentNode.key);
                //get the first node from the bucket's linked list
                HashNode<T, Integer> head = bucketArray.get(bucketIndex);
                //create a new node containing the key, its associated value and its hashcode
                HashNode<T, Integer> newNode = new HashNode(currentNode.key, currentNode.value, hashCode);
                //put it to the beginning of the linked list
                newNode.nextNode = head;
                //replace the existing node from the position corresponding to the key with the new node
                bucketArray.set(bucketIndex, newNode);
                currentNode = currentNode.nextNode;
            }
        }
    }

    //alpha = numberOfElements/numberOfBuckets
    //O(1/(1-alpha))
    public Integer get(T key) {
        //get the index of the bucket corresponding to the given key and its hash code
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);

        //get the first node from the bucket's linked list
        HashNode<T, Integer> head = bucketArray.get(bucketIndex);
        while (head != null) {
            //if the key exists in a node then we return its value
            if (elementIsEqualToNode(head, key, hashCode)) return head.value;
            head = head.nextNode;
        }
        //return null if the key doesn't exist
        return null;
    }

    //alpha = numberOfElements/numberOfBuckets
    //O(1/(1-alpha))
    public Integer add(T key) {
        //get the index of the bucket corresponding to the given key and its hash code
        //in order to check if the key already exists
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);

        //get the first node from the bucket's linked list
        HashNode<T, Integer> head = bucketArray.get(bucketIndex);
        while (head != null) {
            //if the key already exists then return its existing value
            if (elementIsEqualToNode(head, key, hashCode)) {
                return head.value;
            }
            head = head.nextNode;
        }

        //if the key wasn't already present in the table:

        //get the bucket node corresponding to this key
        head = bucketArray.get(bucketIndex);
        //create a new node containing the key, its associated value and its hashcode
        HashNode<T, Integer> newNode = new HashNode(key, numberOfElements, hashCode);
        //put it to the beginning of the linked list
        newNode.nextNode = head;
        //replace the existing node from the position corresponding to the key with the new node
        bucketArray.set(bucketIndex, newNode);

        //if the ratio between the number of elements and number of buckets is greater
        //than the threshold, then the bucket array will be resized
        if ((double) numberOfElements / numberOfBuckets >= THRESHOLD) resize();
        //increment the number of elements
        numberOfElements++;
        return numberOfElements - 1;
    }

    //alpha = numberOfElements/numberOfBuckets
    //O(1/(1-alpha))
    public Integer remove(T key) {
        //get the index of the bucket corresponding to the given key and its hash code
        int bucketIndex = getBucketIndex(key);
        int hashCode = hashCode(key);

        //get the first node from the bucket's linked list
        HashNode<T, Integer> head = bucketArray.get(bucketIndex);

        //keep track of the node that's before the node containing the key
        HashNode<T, Integer> prev = null;
        while (head != null) {
            //we stop searching when we find the node containing the key
            if (elementIsEqualToNode(head, key, hashCode)) break;
            //keep a reference to the current node
            prev = head;
            //go to the next node
            head = head.nextNode;
        }
        //if we didn't find the key, we return null
        if (head == null) return null;

        //otherwise, we decrement the number of elements...
        numberOfElements--;

        //...and check if the node which we want to remove was the first in the linked list
        //if it was, just make the next node the head
        if (prev == null) bucketArray.set(bucketIndex, head.nextNode);
            //otherwise, the previous node will point to the second successor(the next node corresponding to the removed node)
        else prev.nextNode = head.nextNode;
        return head.value;
    }
    @Override
    public String toString() {
        List<HashNode<T, Integer>> items = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Symbol Table Information:\n");
        stringBuilder.append("Data Structure: Hash Table\n");
        stringBuilder.append("Hash Table Size: %s\n".formatted(numberOfBuckets));
        stringBuilder.append("Number of items: %s\n".formatted(numberOfElements));
        stringBuilder.append("Collision Resolution: Chaining\n\n");

        stringBuilder.append("  Symbol       Position  \n");
        stringBuilder.append("-------------------------\n");
        for (var node : bucketArray) {
            while(node != null){
                items.add(node);
                node = node.nextNode;
            }
        }

        items.sort(Comparator.comparingInt(item -> item.value));

        for(var item : items)
            stringBuilder.append("%-20s %3d\n".formatted(item.key, item.value));
        return stringBuilder.toString();
    }
}