package TSB;

import java.io.Serializable;
import java.util.*;

public class HashTable<K,V> implements Map<K,V>, Cloneable,Serializable,Iterable
{
    private final static int MAX_SIZE = Integer.MAX_VALUE;
    private Entry<K,V> table[];
    private int initial_capacity;
    private int count;
    private float load_factor;
    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;

    @Override
    public Iterator iterator() {
        return null;
    }

    private class Iterator implements java.util.Iterator<Entry<K,V>> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Entry<K, V> next() {
            return null;
        }

        @Override
        public void remove() {

        }
    }

    public enum Estado {
        OCUPADO,
        VACIO,
        TUMBA,
    }

    protected transient int modCount;

    public HashTable()
    {
        this(5, 0.8f);
    }

    public HashTable(int initial_capacity, float load_factor)
    {
        if(load_factor <= 0) { load_factor = 0.8f; }
        if(initial_capacity <= 0) { initial_capacity = 11; }
        else
        {
            if(initial_capacity > HashTable.MAX_SIZE)
            {
                initial_capacity = HashTable.MAX_SIZE;
            }
        }

        this.table = new Entry[initial_capacity];


        this.initial_capacity = initial_capacity;
        this.load_factor = load_factor;
        this.count = 0;
        this.modCount = 0;
    }

    public int h(int k, int t) {
        if (k <= 0) {
            k*= -1;
        }
        return k % t;
    }

    private int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }

    private int h(K key)
    {
        return h(key.hashCode(), initial_capacity);
    }



    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return (this.count == 0);
    }

    @Override
    public boolean containsKey(Object key) {
        return (this.get((K)key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {

        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    private class Entry<K, V> implements Map.Entry<K, V>
    {
        private K key;
        private V value;
        private Estado estado;


        public Entry(K key, V value)
        {
            if(key == null || value == null)
            {
                throw new IllegalArgumentException("Entry(): parámetro null...");
            }
            this.key = key;
            this.value = value;
            estado = Estado.OCUPADO;

        }

        @Override
        public K getKey()
        {
            return key;
        }

        @Override
        public V getValue()
        {
            return value;
        }

        @Override
        public V setValue(V value)
        {
            if(value == null)
            {
                throw new IllegalArgumentException("setValue(): parámetro null...");
            }

            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }

            final Entry other = (Entry) obj;
            if (!Objects.equals(this.key, other.key)) { return false; }
            if (!Objects.equals(this.value, other.value)) { return false; }
            return true;
        }

        @Override
        public String toString()
        {
            return "(" + key.toString() + ", " + value.toString() + ")";
        }
    }

    /*
     * Clase interna que representa una vista de todas los Claves mapeadas en la
     * tabla: si la vista cambia, cambia también la tabla que le da respaldo, y
     * viceversa. La vista es stateless: no mantiene estado alguno (es decir, no
     * contiene datos ella misma, sino que accede y gestiona directamente datos
     * de otra fuente), por lo que no tiene atributos y sus métodos gestionan en
     * forma directa el contenido de la tabla. Están soportados los metodos para
     * eliminar un objeto (remove()), eliminar todo el contenido (clear) y la
     * creación de un Iterator (que incluye el método Iterator.remove()).
     */


}
