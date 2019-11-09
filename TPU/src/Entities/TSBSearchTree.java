
package Entities;

import java.io.Serializable;
import java.util.*;

/**
 * Representa un árbol binario de búsqueda SIN estrategia de autoequilibrado 
 * (sólo a efectos didácticos). Cada nodo del árbol tiene referencias a sus dos 
 * hijos, pero no tiene una referencia al padre, por lo que muchos procesos que
 * requieren navegación hacia arriba en el árbol, se hace en forma recursiva. 
 * 
 * Dado que no se aplica estrategia alguna de re-equilibrado, en el peor caso 
 * las operaciones de recorrido que implican buscar un elemento, insumen un 
 * tiempo O(n).
 * 
 * La clase de los objetos que serán insertados en el árbol deberia implementar
 * la interface Comparable y/o la interface Comparator, o bien debe proveerse un
 * objeto Comparator, pues de lo contrario las operaciones que impliquen 
 * búsqueda en el árbol fallarán lanzando una excepción.
 * 
 * @author Ing. Valerio Frittelli.
 * @version Octubre de 2017.
 * @param <E> la clase de los objertos que serán insertados en el árbol.
 */
public class TSBSearchTree<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable
{
    //********** Atributos privados.
    
    // referencia al nodo raíz del árbol...
    private TreeNode<E> root;
        
    // la cantidad de elementos que contiene el árbol...
    private int count;
    
    // conteo de operaciones de cambio de tamaño (fail-fast iterator).
    protected transient int modCount;
       
    
    //********** Atributos privados constantes.

    // el comparador usado para establecer el orden en el árbol, o null si el 
    // árbol usa el ordenamiento natural (definido por compareTo())...
    private final Comparator<? super E> comparator;


    //********** Constructores.

    /**
     * Construye un nuevo árbol vacío, que se ordenará de acuerdo al criterio
     * natural (definido por compareTo()). La clase de los objetos insertados en 
     * el árbol DEBE implementar entonces la interface Comparable, y si no se 
     * aplica generics para hacer control de homogeneidad, entonces también se
     * debe garantizar que los objetos contenidos en el árbol sean mutuamente
     * comparables (de lo contrario, se lanzará una excepción de conversión de 
     * tipos).
     */
    public TSBSearchTree() 
    {
        this.init(null, 0, 0);
        this.comparator = null;
    }

    /**
     * Construye un nuevo árbol vacío, que será ordenado de acuerdo al criterio
     * definido por el comparator especificado. Si no se aplica genercis para 
     * hacer control homogeneidas, entonces debe garantizarse que los objetos 
     * insertados sean mutuamente comparables con el comparador especificado (de    
     * lo contrario, puede ser lanzada una excepción de conversión de tipos).
     * @param comparator el comparador que será usado para ordenar el árbol. Si 
     *        su valor es null, se aplicará entonces el criterio natural basado
     *        en compareTo().
     */
    public TSBSearchTree(Comparator<? super E> comparator) 
    {
        this.init(null, 0, 0);
        this.comparator = comparator;
    }

    /**
     * Construye un nuevo árbol conteniendo todos los elementos incluidos en la
     * colección c especificada, ordenados en forma natural (es decir, usando
     * compareTo()). La clase de los elementos insertados debe implementar 
     * entonces Comparable, y todos los elementos deben ser mutuamente 
     * comparables para evitar una excepción de conversión de tipos.
     * @param c la colección cuyos elementos serán copiados en el árbol.
     * @throws ClassCastException si los elementos de c no son Comparables, o 
     *         no son mutuamente comparables.
     * @throws NullPointerException si c es null.
     */
    public TSBSearchTree(Collection<? extends E> c) 
    {
        this();
        this.addAll(c);
    }

    /**
     * Construye un nuevo árbol conteniendo los mismos elementos y usando el 
     * mismo criterio de ordenamiento del conjunto especificado por s.     *
     * @param s el conjunto ordenado cuyos elementos serán copiados en el árbol.
     * @throws NullPointerException si s es null.
     */
    public TSBSearchTree(SortedSet<E> s) 
    {
        this(s.comparator());
        addAll(s);
    }
    
    
    //********** Implementación de métodos especificados por Collection.   
    
    /**
     * Agrega el elemento especificado e al árbol, siempre y cuando no estuviese
     * ya contenido.
     * @param e el elemento a agregar al árbol.
     * @return true si el agregado pudo hacerse.
     * @throws NullPointerException si el parámetro e es null.
     */
    @Override
    public boolean add(E e)
    {
        if(e == null) { throw new NullPointerException("add(): parámetro null..."); }

        TreeNode<E> p = this.root, q = null;
        while(p != null)
        {
            E y = p.getInfo();
            if(this.compare(e, y) == 0) { break; }

            q = p;
            if(this.compare(e, y) < 0) { p = p.getLeft(); }
            else { p = p.getRight(); }
        }

        // si ya existia... retorne false.
        if(p != null) { return false; }

        // si no existia... hay que insertarlo.
        TreeNode<E> nuevo = new TreeNode<>(e, null, null);
        if(q == null) { this.root = nuevo; }
        else 
        {
           if (this.compare(e, q.getInfo()) < 0) { q.setLeft(nuevo); }
           else { q.setRight(nuevo); }
        }
        
        this.count++;
        this.modCount++;

        return true;        
    }
            
    /**
     * Determina si el árbol contiene un elemento que coincida con o. Retorna
     * true en ese caso, o false si el árbol no contiene a o.
     * @param o el objeto a buscar en el árbol.
     * @return true si el árbol contiene a o.
     */
    @Override
    public boolean contains(Object o)
    {   
        if(o == null) { return false; }

        E x = (E)o;
        TreeNode<E> p = this.root;
        while(p != null)
        {
            E y = p.getInfo();
            if(this.compare(x, y) == 0) { return true; }
            if(this.compare(x, y) < 0) { p = p.getLeft(); }
            else { p = p.getRight(); }
        }
        return false;        
    }
        
    /**
     * Elimina del árbol el nodo que contiene al objeto o, si existía (y retorna
     * true en ese caso). Retorna false en caso contrario (y también si el 
     * objeto o es null).
     * @param o el objeto a buscar y eliminar del árbol.
     * @return true si la eliminación tuvo éxito.
     */
    @Override
    public boolean remove(Object o)
    {
        if(o == null) { return false; }
        int ca = this.size();
        root = this.delete_node(root, (E)o);
        return (this.size() != ca);
    }
    
    
    //********** Implementación de métodos especificados por AbstractCollection.
    
    /**
     * Retorna un iterador de tipo ascendente para este árbol (con este iterador
     * el contenido del árbol será recorrido y recuperado en orden de menor a 
     * mayor, de acuero al tipo de comparador que se haya especificado).
     * @return un iterador ascendente para este árbol.
     */
    @Override
    public Iterator<E> iterator() 
    {
        return new AscendingTSBSearchTreeIterator();
    }

    /**
     * Retorna la cantidad de elementos (o tamaño) del árbol.
     * @return la cantidad de elementos del árbol.
     */
    @Override
    public int size() 
    {
        return count;
    }

    
    //********** Implementación de métodos especificados por SortedSet.
    
    /**
     * Retorna el objeto que contiene al método de comparación usado en este 
     * árbol (si se especificó alguno al crear el árbol) o retorna null si la
     * estrategia de comparación es la natural, impuesta por compareTo(). 
     * @return el comparador especificado para este árbol, o null si se emplea
     *         compareTo() en forma natural.
     */
    @Override
    public Comparator<? super E> comparator() 
    {
        return this.comparator;
    }

    /**
     * Retorna el primer elemento del árbol, entendiéndose a este como el menor
     * elemento del árbol. 
     * @return el menor de los elementos contenidos en árbol.
     * @throws NoSuchElementException si el árbol está vacío.
     */
    @Override
    public E first() 
    {
        if(this.isEmpty()) 
        {
            throw new NoSuchElementException("first(): árbol vacío...");
        }
        
        TreeNode<E> p = this.root;
        while(p.getLeft() != null) { p = p.getLeft(); }
        return p.getInfo();
    }

    /**
     * Retorna el último elemento del árbol, entendiéndose a este como el mayor
     * elemento del árbol. 
     * @return el mayor de los elementos contenidos en árbol.
     * @throws NoSuchElementException si el árbol está vacío.
     */
    @Override
    public E last() 
    {
        if(this.isEmpty()) 
        {
            throw new NoSuchElementException("first(): árbol vacío...");
        }
        
        TreeNode<E> p = this.root;
        while(p.getRight() != null) { p = p.getRight(); }
        return p.getInfo();
    }
    
    
    //********** Implementación de métodos especificados por NavigableSet.

    /**
     * Retorna el elemento más grande de este árbol, que a su vez sea 
     * estrictamente menor que el elemento e especificado como parámetro, o bien
     * retorna null si no hay un elemento que cumpla ese requisito en el árbol. 
     * Al no contar con una referencia al nodo padre en cada nodo, este método
     * hace un recorrido con iterador ascendente, lo cual podria llevar un 
     * tiempo O(n) en el peor caso.
     * @param e el elemento usado como referencia para la búsqueda.
     * @return el mayor elemento del árbol que sea estrictamente menor que e.
     * @throws ClassCastException si el elemento especificado no puede ser 
     *         comparado con los elementos del árbol.
     * @throws NullPointerException si el parámetro e es null.
     */
    @Override
    public E lower(E e) 
    {
        if(e == null) { throw new NullPointerException("lower(): parámetro null..."); }
        if(this.isEmpty()) { return null; }

        E q = null;
        Iterator<E> it = this.iterator();
        while(it.hasNext())
        {
            E p = it.next();
            if(this.compare(e, p) < 0) { return q; }
            q = p;
        }
        
        return null;
    }

    /**
     * Retorna el elemento más grande de este árbol, que a su vez sea 
     * menor o igual que el elemento e especificado como parámetro, o bien
     * retorna null si no hay un elemento que cumpla ese requisito en el árbol. 
     * Al no contar con una referencia al nodo padre en cada nodo, este método
     * hace un recorrido con iterador ascendente, lo cual podria llevar un 
     * tiempo O(n) en el peor caso.
     * @param e el elemento usado como referencia para la búsqueda.
     * @return el mayor elemento del árbol que sea estrictamente menor que e.
     * @throws ClassCastException si el elemento especificado no puede ser 
     *         comparado con los elementos del árbol.
     * @throws NullPointerException si el parámetro e es null.
     */
    @Override
    public E floor(E e) 
    {
        if(e == null) { throw new NullPointerException("lower(): parámetro null..."); }
        if(this.isEmpty()) { return null; }

        E q = null;
        Iterator<E> it = this.iterator();
        while(it.hasNext())
        {
            E p = it.next();
            if(this.compare(e, p) <= 0) { return q; }
            q = p;
        }
        
        return null;  
    }

    /**
     * Retorna el elemento más chico de este árbol, que a su vez sea 
     * mayor o igual que el elemento e especificado como parámetro, o bien
     * retorna null si no hay un elemento que cumpla ese requisito en el árbol. 
     * Al no contar con una referencia al nodo padre en cada nodo, este método
     * hace un recorrido con iterador ascendente, lo cual podria llevar un 
     * tiempo O(n) en el peor caso.
     * @param e el elemento usado como referencia para la búsqueda.
     * @return el mayor elemento del árbol que sea estrictamente menor que e.
     * @throws ClassCastException si el elemento especificado no puede ser 
     *         comparado con los elementos del árbol.
     * @throws NullPointerException si el parámetro e es null.
     */    
    @Override
    public E ceiling(E e) 
    {
        if(e == null) { throw new NullPointerException("floor(): parámetro null..."); }
        if(this.isEmpty()) { return null; }

        Iterator<E> it = this.iterator();
        while(it.hasNext())
        {
            E p = it.next();
            if(this.compare(p, e) >= 0) { return p; }
        }
        
        return null;  
    }

    /**
     * Retorna el elemento más chico de este árbol, que a su vez sea 
     * estrictamente mayor que el elemento e especificado como parámetro, o bien
     * retorna null si no hay un elemento que cumpla ese requisito en el árbol. 
     * Al no contar con una referencia al nodo padre en cada nodo, este método
     * hace un recorrido con iterador ascendente, lo cual podria llevar un 
     * tiempo O(n) en el peor caso.
     * @param e el elemento usado como referencia para la búsqueda.
     * @return el mayor elemento del árbol que sea estrictamente menor que e.
     * @throws ClassCastException si el elemento especificado no puede ser 
     *         comparado con los elementos del árbol.
     * @throws NullPointerException si el parámetro e es null.
     */
    @Override
    public E higher(E e) 
    {
        if(e == null) { throw new NullPointerException("floor(): parámetro null..."); }
        if(this.isEmpty()) { return null; }

        Iterator<E> it = this.iterator();
        while(it.hasNext())
        {
            E p = it.next();
            if(this.compare(p, e) > 0) { return p; }
        }
        
        return null;  
    }

    /**
     * Retorna y remueve del árbol el primer elemento (o sea, el menor), o 
     * retorna null si el árbol está vacío.
     * @return el menor elemento del árbol o null si el árbol está vacío.
     */
    @Override
    public E pollFirst() 
    {
        E r = this.first();
        boolean ok = this.remove(r);
        return (ok)? r : null;
    }

    /**
     * Retorna y remueve del árbol el último elemento (o sea, el mayor), o 
     * retorna null si el árbol está vacío.
     * @return el mayor elemento del árbol o null si el árbol está vacío.
     */
    @Override
    public E pollLast() 
    {
        E r = this.last();
        boolean ok = this.remove(r);
        return (ok)? r : null;
    }

    @Override
    public NavigableSet<E> descendingSet() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<E> descendingIterator() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedSet<E> headSet(E toElement) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    //************************ Redefinición de métodos heredados desde Object.
    
    /**
     * Retorna una copia superficial del árbol. Los nodos que conforman el árbol
     * se clonan ellos mismos, pero no se clonan los objetos que esos nodos 
     * contienen: en cada node de nuevo árbol se almacenan las direcciones de 
     * los mismos objetos que contiene el original. 
     * @return una copia superficial del árbol.
     * @throws CloneNotSupportedException si la clase no implementa la
     *         interface Cloneable.    
     */ 
    @Override
    protected Object clone() throws CloneNotSupportedException 
    {
        TSBSearchTree<E> t = (TSBSearchTree<E>)super.clone();
        t.root = null;
        t.count = 0;

        for(E x : this) 
        {
            t.add(x);
        }
        t.modCount = 0;
        return t;
    }

    /**
     * Determina si este árbol es igual al objeto especificado.
     * @param obj el objeto a comparar con esta tabla.
     * @return true si los objetos son iguales.
     */
    @Override
    public boolean equals(Object obj) 
    {
        if(!(obj instanceof TSBSearchTree)) { return false; }
        
        TSBSearchTree<E> t = (TSBSearchTree<E>)obj;
        if(t.size() != this.size()) { return false; }

        try 
        {
            for(E x : this)
            {
                if(!t.contains(x)) { return false; }
            }
        } 
        
        catch(ClassCastException | NullPointerException e) 
        {
            return false;
        }

        return true;    
    }

    /**
     * Retorna un hash code para el árbol completo.
     * @return un hash code para la tabla.
     */
    @Override
    public int hashCode() 
    {
        if(this.isEmpty()) {return 0;}
        
        int hc = 0;
        for(E x : this)
        {
            hc += x.hashCode();
        }
        
        return hc;
    }
    
    /**
     * Devuelve el contenido del árbol en forma de String. El recorrido del 
     * árbol se hace en entre orden, por lo que los elementos del árbol aparecen
     * de menor a mayor en la cadena retornada.
     * @return una cadena con el contenido completo de la tabla.
     */
    @Override
    public String toString() 
    {
        StringBuffer cad = new StringBuffer("");
        make_center_order(this.root, cad);
        return cad.toString();       
    }
    
    /**
     * Devuelve el contenido del árbol en forma de String. El recorrido del 
     * árbol se hace en pre orden.
     * @return una cadena con el contenido completo de la tabla.
     */
    public String toStringPreOrder()
    {
        StringBuffer cad = new StringBuffer("");
        make_pre_order(this.root, cad);
        return cad.toString();       
    }

    /**
     * Devuelve el contenido del árbol en forma de String. El recorrido del 
     * árbol se hace en post orden.
     * @return una cadena con el contenido completo de la tabla.
     */
    public String toStringPostOrder()
    {
        StringBuffer cad = new StringBuffer("");
        make_post_order(this.root, cad);
        return cad.toString();       
    }

    //************************ Clases internas.
    
    /*
     * Clase interna que representa los nodos que se almacenan en el árbol.
     * El constructor lanzará una IllegalArgumentException si el parámetro a 
     * asignar como info es null.
     */
    protected static class TreeNode<E>
    {
        private E info;
        private TreeNode<E> left;
        private TreeNode<E> right;
        
        public TreeNode(E info, TreeNode<E> left, TreeNode<E> right) 
        {
            if(info == null)
            {
                throw new IllegalArgumentException("Node(): parámetro null...");
            }
            this.info = info;
            this.left = left;
            this.right = right;
        }
        
        public E getInfo() 
        {
            return this.info;
        }
        
        // use con cuidado... y a su propio riesgo...
        private void setInfo(E info)
        {
            this.info = info;
        }
                
        public TreeNode<E> getLeft() 
        {
            return this.left;
        }

        public TreeNode<E> getRight() 
        {
            return this.right;
        }

        public void setLeft(TreeNode<E> newLeft) 
        {
            this.left = newLeft;
        }

        public void setRight(TreeNode<E> newRight) 
        {
            this.right = newRight;
        }
        
        @Override
        public int hashCode() 
        {
            int hash = 7;
            hash = 61 * hash + this.info.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) 
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }
            
            final TreeNode<E> other = (TreeNode<E>) obj;
            if (!Objects.equals(this.info, other.info)) { return false; }
            return true;
        }       
        
        @Override
        public String toString()
        {
            return this.info.toString();
        }
    }
    
    private class AscendingTSBSearchTreeIterator implements Iterator<E>
    {
            // elemento actual en el iterador (el que fue retornado la última 
            // vez por next() y será eliminado por remove())...
            private TreeNode<E> current;
            
            // elemento que fue retornaro inmediatamente antes que current (es
            // necesario para el método remove() del iterador)...
            private TreeNode<E> before_current;

            // flag para controlar si remove() está bien invocado...
            private boolean next_ok;

            // una pila para ir avanzado en el árbol en forma asíncrona...
            ArrayList<TreeNode<E>> stack;

            // el valor que debería tener el modCount del árbol...
            private int expected_modCount;
            
            /*
             * Crea un iterador comenzando en la primera lista. Activa el 
             * mecanismo fail-fast.
             */
            public AscendingTSBSearchTreeIterator()
            {
                current = null;
                before_current = null;
                next_ok = false;
                stack = new ArrayList<>();
                expected_modCount = TSBSearchTree.this.modCount;
            }

            /*
             * Determina si hay al menos un elemento en el árbol que no haya 
             * sido retornado por next(). 
             */
            @Override
            public boolean hasNext() 
            {
                if(TSBSearchTree.this.isEmpty()) { return false; }
                if(current != null && current.getRight() == null && stack.isEmpty())
                {
                    return false;
                }

                // en principio alcanza con esto... revisar...    
                return true;
            }

            /*
             * Retorna el siguiente elemento disponible en el árbol.
             */
            @Override
            public E next() 
            {
                // control: fail-fast iterator...
                if(TSBSearchTree.this.modCount != expected_modCount)
                {    
                    throw new ConcurrentModificationException("next(): modificación inesperada del árbol...");
                }

                if(!hasNext()) 
                {
                    throw new NoSuchElementException("next(): no existe el elemento pedido...");
                }
                
                if(current == null) 
                { 
                    before_current = null;
                    if(stack.isEmpty()) 
                    { 
                        current = TSBSearchTree.this.root; 
                        stack.add(0, current);
                        while(current.getLeft() != null) 
                        {
                            current = current.getLeft(); 
                            stack.add(0, current);
                        }                    
                    }
                    else { current = stack.get(0); }
                }
                else
                {    
                    // no es null, ya fue retornado y salió del stack...
                    before_current = current;
                    if(current.getRight() == null)
                    {
                        // ... si no tiene hijo derecho, subir en el stack...
                        current = stack.get(0);
                    }
                    else
                    {
                        // si tiene derecho, buscar el descendiente más lejano
                        // a la izquierda de ese...
                        current = current.getRight();
                        stack.add(0, current);
                        while(current.getLeft() != null) 
                        {
                            current = current.getLeft(); 
                            stack.add(0, current);
                        }
                    }
                }
                
                // avisar que next() fue invocado con éxito...
                next_ok = true;

                // tomar el info del nodo alcanzado...
                E value = current.getInfo();
                
                // como será retornado, removerlo del stack... y terminar...
                stack.remove(0);
                return value;
            }

            /*
             * Remueve el elemento actual del árbol, dejando el iterador en la
             * posición anterior al que fue removido. El elemento removido es el
             * que fue retornado la última vez que se invocó a next(). El método
             * sólo puede ser invocado una vez por cada invocación a next().
             */
            @Override
            public void remove() 
            {
                if(!next_ok) 
                { 
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()..."); 
                }
                
                // por comodidad, se invoca al método remove() del árbol...
                // ...recordar que ese método resta 1 a count y suma 1 a 
                // modCount si la eliminación tuvo éxito...
                if(TSBSearchTree.this.remove(current.getInfo()))
                {
                    // volver al que se retornó antes de current...
                    this.current = this.before_current;
                    
                    // fail_fast iterator: todo en orden...
                    expected_modCount++;                    
                }

                // avisar que el remove() válido para next() ya se activó...
                next_ok = false;
            }     
        }

    
    //********** Métodos privados.
    
    // Auxiliar para los constructores.
    private void init(TreeNode<E> r, int c, int m)
    {
        this.root = r;
        this.count = c;
        this.modCount = m;
    }
    
    // Compara dos objetos con el método de comparación correcto de este árbol.
    @SuppressWarnings("unchecked")
    private int compare(Object k1, Object k2) 
    {
        return (comparator == null)? ((Comparable<? super E>)k1).compareTo((E)k2)
            : comparator.compare((E)k1, (E)k2);
    }
    
    // crea una cadena con el contenido del árbol en entre orden...
    private void make_center_order(TreeNode<E> p, StringBuffer cad)
    {
        if (p != null) 
        {
            make_center_order (p.getLeft(), cad);
            cad = cad.append(p.getInfo().toString()).append(" ");         
            make_center_order (p.getRight(), cad);
        }
    }
    
    // crea una cadena con el contenido del árbol en pre orden...
    private void make_pre_order(TreeNode<E> p, StringBuffer cad)
    {
        if (p != null) 
        {
            cad = cad.append(p.getInfo().toString()).append(" ");         
            make_pre_order (p.getLeft(), cad);
            make_pre_order (p.getRight(), cad);
        }
    }

    // crea una cadena con el contenido del árbol en post orden...
    private void make_post_order(TreeNode<E> p, StringBuffer cad)
    {
        if (p != null) 
        {
            make_post_order (p.getLeft(), cad);       
            make_post_order (p.getRight(), cad);
            cad = cad.append(p.getInfo().toString()).append(" ");  
        }
    }

    /*
     * Auxiliar de remove(). Elimina un nodo que contenga al objeto x si el 
     * mismo tiene un sólo hijo o ninguno.
     * @param p el nodo que esta siendo procesado.
     * @param x el objeto a buscar y elimnar del árbol.
     * @return direccion del nodo que quedó en lugar del que venia en p al 
               comenzar el proceso.
     */
    private TreeNode<E> delete_node(TreeNode<E> p, E x)
    {
        if (p != null)
        {
            E y = p.getInfo();
            if(this.compare(x, y) < 0) 
            { 
                TreeNode<E> menor = delete_node(p.getLeft(), x);
                p.setLeft(menor);   
            }
            else
            {
                if(this.compare(x, y) > 0) 
                { 
                    TreeNode<E> mayor = delete_node (p.getRight(), x);
                    p.setRight(mayor); 
                } 
                else
                {  
                    // Objeto encontrado... debe borrarlo.
                    if (p.getLeft() == null) { p = p.getRight(); }
                    else
                    {
                        if (p.getRight() == null) { p = p.getLeft(); }
                        else 
                        {
                            // Tiene dos hijos... pasar el pedido...
                            TreeNode<E> dos = delete_with_two_children(p.getLeft(), p);
                            p.setLeft(dos);
                        }
                    }
                    this.count--;
                    this.modCount++;
                }
            }
        }
        return p;
    }
    
    /*
     * Auxiliar del metodo remove(). Reemplaza al nodo que venía en p por su 
     * mayor descendiente izquierdo d, y luego borra a d de su posicion original.
     * @param d el nodo que esta siendo procesado.
     * @param p el nodo a reemplazar por d.
     * @return direccion del nodo que quedo en lugar del que venia en d al 
     *         comenzar el proceso.
    */    
    private TreeNode<E> delete_with_two_children(TreeNode<E> d, TreeNode<E> p)
    {
        if (d.getRight() != null) 
        { 
            TreeNode<E> der = delete_with_two_children(d.getRight(), p);
            d.setRight(der); 
        }
        else 
        {
            p.setInfo(d.getInfo());
            d = d.getLeft();
        }
        return d;
    }
}
