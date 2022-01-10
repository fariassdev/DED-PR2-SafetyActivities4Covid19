package uoc.ded.practica.util;

import java.util.Comparator;

import uoc.ei.tads.ContenedorAcotado;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.IteradorVectorImpl;

/**
 * TAD que implementa un vector ordenado. La ordenación del vector
 * se determina con el comparador
 */
public class OrderedVector<E> implements ContenedorAcotado<E>{


    private Comparator<E> comparator;

    private E[] data;
    private int len;


    public OrderedVector(int max, Comparator<E> comparator) {
        this.comparator = comparator;
        data = (E[])new Object[max];
        len = 0;
    }

    public E elementAt(int i) {
        return this.data[i];
    }

    /**
     * método que indica si un elemento es igual que el segundo
     * @param elem1
     * @param elem2
     * @return
     */
    private boolean compare(E elem1, E elem2) {
        return ((Comparable<E>)elem1).compareTo(elem2) == 0;
    }

    public void rshift(int i) {
        // desplazamiento de todos los elementos una posición
        int p = len-1;
        while (p >= i) {
            data[p+1] = data[p];
            p--;
        }
    }

    public void lshift(int i) {
        // desplazamiento de todos los elementos una posición
        int p = i;
        while (p < len-1) {
            data[p] = data[p+1];
            p++;
        }
    }


    public void update(E vIn) {
        int i = 0;

        // Si existe el elemento borramos el elemento para volverlo a insertar en su posición
        if (estaLleno()) {
            E pOut = last();
            if (comparator.compare(pOut, vIn) < 0) {
                delete(pOut);
                update(vIn);
                return;

            }
            else {
                return;
            }
        }

        // hacemos un recorrido para determinar la posición en la que insertar
        while (i < len && data[i] != null && comparator.compare(data[i], vIn) >= 0) { 
            i++;
        }

        // desplazamiento hacia la derecha de todos los elementos
        rshift(i);

        // se inserta el elemento en la posición
        data[i] = vIn;
        len++;

    }

    public void delete (E elem) {
        int i = 0;
        boolean found = false;

        while (!found && i < len)
            found = compare(elem, data[i++]);

        if (found) {
            if (i<len) {
                lshift(i-1);
            }
            else {
            	lshift(i);
            }
            len--;
        }
    }

    public Iterador<E> elementos() {
        return (Iterador<E>)new IteradorVectorImpl(data, len,0);
    }


    public boolean estaVacio() {
        return len == 0;
    }


    public int numElems() {
        return len;
    }

    public boolean estaLleno() {
        return len == data.length;
    }

    /**
     * método de test
     * @param args
     */
    public static void main(String[] args) {
        Comparator<Integer> cmp = new Comparator<Integer>() {
            public int compare(Integer arg0, Integer arg1) {
                return arg0.compareTo(arg1);
            }
        };
        OrderedVector<Integer> v = new OrderedVector<Integer>(10, cmp);

        v.update(7);
        v.update(9);
        v.update(5);
        v.update(2);
        v.update(3);
        v.update(1);
        v.update(4);
        v.update(6);
        v.update(11);
        v.update(12);

        System.out.println("estaLleno " + v.estaLleno());

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("Delete 1");
        v.delete(1);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("add 8  and full vector");
        v.update(8);

        System.out.println("estaLleno " + v.estaLleno());

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("estaLleno " + v.estaLleno());

        System.out.println("delete 3");
        v.delete(3);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("estaLleno " + v.estaLleno());

        System.out.println("add 25!");
        v.update(25);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("estaLleno " + v.estaLleno());

        System.out.println("add 32");
        v.update(32);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("add 15");
        v.update(15);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("add 3");
        v.update(3);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }

        System.out.println("add 40");
        v.update(40);

        for (Iterador<Integer> it = v.elementos(); it.haySiguiente();) {
            System.out.println(it.siguiente());
        }
    }

    public E last() {
        return data[len-1];
    }
}
