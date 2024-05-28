package luppolo.nodi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodoAddizione extends NodoOperazione {

  // TODO: è mutable perché ordinaFigli mi permette di ordinare tale attributo, oppure no perché è
  // protected??

  private List<Nodo> figli = new ArrayList<>();

  /*
   * due costruttori, uno per la notazione polacca, che si aspetta due nodi, e l'altro per il programma lineare, che si aspetta la lista
   */

  public NodoAddizione(String op, Nodo n1, Nodo n2) {
    super("+");
    if (op.equals("+")) {

      figli.add(n1);
      figli.add(n2);
    } else {

      figli.add(n1);
      figli.add(new NodoMoltiplicazione("*", new Razionale("-1"), n2));
    }
  }

  public NodoAddizione(String op, List<Nodo> l) {
    super("+");
    if (op.equals("+")) {

      for (Nodo nodo : l) {
        figli.add(nodo);
      }
    } else {

      figli.add(l.get(0));
      l = l.subList(1, l.size());
      for (Nodo nodo : l) {
        figli.add(new NodoMoltiplicazione("*", new Razionale("-1"), nodo));
      }
    }
  }

  public List<Nodo> getFigli() {
    return figli;
  }

  protected void ordinaFigli() {
    Collections.sort(this.figli);
  }

  @Override
  public int compareTo(Nodo n) {
    if (n instanceof NodoAddizione) {
      return comparaFigliStessoTipo((NodoOperazione) n); // sto comparando due nodi addizione
    }
    return 1; // Addizione viene dopo tutti
  }


    // TODO continua hrthjerasjsssssjsjsjsjsjsjsjsjsjsjsjsjsjsjsjsjsjsjs

  @Override
  public Nodo semplificaNodo() {
    
    for (Nodo j : this.getFigli()) {
      if (j instanceof Foglia) {
        System.out.println(j);
      }else{
        System.out.println(((NodoOperazione) j).stampaNodo());
      }
      
    }

    List<Razionale> razionali = new ArrayList<>();
    List<Nodo> nodi = new ArrayList<>();

    for (Nodo n : this.getFigli()) {
      if (n instanceof Razionale) {
        razionali.add((Razionale) n);
      }else if (n instanceof NodoAddizione) { // i fattori additivi diventano fattori della addizione più esterna
        for (Nodo nodo : ((NodoAddizione) n).getFigli()) {
          if (nodo instanceof Razionale) {
            razionali.add((Razionale) nodo);
          }else{
            nodi.add(nodo);
          }
        }
      }else{
        nodi.add(n);
      }
    }

    for (Nodo j : nodi) {
      if (j instanceof Foglia) {
        System.out.println(j);
      }else{
        System.out.println(((NodoOperazione) j).stampaNodo());
      }
      
    }

    Razionale r = new Razionale(0, 1); // se r rimane 0 lo omettiamo

    for (Razionale i : razionali) {
      r = r.Somma(i);
    }

  Map<Nodo, Razionale> prodotti = new HashMap<>();

   for (Nodo i : nodi) {
    if (!(i instanceof NodoMoltiplicazione)) { // rendo ogni nodo una mul*1
      i = new NodoMoltiplicazione("*", new Razionale(1,1), i);
      i.ordina();
    }
    NodoOperazione n = (NodoOperazione) i;
   
    Nodo m =  n.getFigli().get(1); // prendo il fattore nodo della moltiplicazione che è sempre il secondo per l'ordine
    Nodo k = null;
    for (Nodo t : prodotti.keySet()) {
      if (t.equals(m)) k = t;
    }
  
    

    if (k != null) { // se il prodotto con nodo n è già presente, somma coefficienti
      Razionale temp = prodotti.get(k);
      temp = temp.Somma((Razionale) n.getFigli().get(0));
      // il coeff sta al primo posto per l'ordine definito
    
    prodotti.put(k, temp);
    }else{ // se la potenza non è presente, la aggiungo
  
      prodotti.put(m,  (Razionale) n.getFigli().get(0));
    }

   }

   List<Nodo> nuoviFigli = new ArrayList<>();
   if (!(r.equals(new Razionale(0, 1)))) nuoviFigli.add(r);

   for (Map.Entry<Nodo, Razionale> p : prodotti.entrySet()) {
    if (p.getValue().equals(new Razionale(1, 1))) {
      // se ha coeff 1 aggiungo solo il nodo
      nuoviFigli.add(p.getKey());
    }else if (p.getValue().equals(new Razionale(0, 1))) {
      continue; // non metto nodo*0
    }else{
      NodoMoltiplicazione temp = new NodoMoltiplicazione("*", p.getValue(), p.getKey());
      nuoviFigli.add(temp);
    }
   }

   if (r.equals(new Razionale(0, 1)) && nuoviFigli.size() == 0) return new Razionale(0, 1);
   if (nuoviFigli.size() == 0) return new Razionale(0, 0);
   if (nuoviFigli.size() == 1) {
    Nodo n = nuoviFigli.get(0);
    return n;
   }
   NodoAddizione ris = new NodoAddizione("+", nuoviFigli);
   ris.ordina();
    return ris;
  }

  // TODO prova semplificazione

  public static void main(String[] args) {
    

    List<Nodo> l = List.of(
      new Razionale("-3"), new Razionale("3"), 
      new NodoMoltiplicazione("*", new Razionale("4"), new NodoAddizione("+", new Variabile("x"), new Variabile("y"))),
      new NodoMoltiplicazione("*", new Razionale("4"), new NodoAddizione("+", new Variabile("x"), new Variabile("y")))

      );
    Nodo n = new NodoAddizione("+",l);
   
  
    n = ((NodoAddizione) n).semplificaNodo();
    if (n instanceof Foglia) {
      System.out.println(n);
    }else{
      System.out.println(((NodoOperazione) n).stampaNodo());
    }

  }

}
