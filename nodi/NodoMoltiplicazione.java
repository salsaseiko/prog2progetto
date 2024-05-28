package luppolo.nodi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NodoMoltiplicazione extends NodoOperazione {

  private List<Nodo> figli = new ArrayList<>();

  public NodoMoltiplicazione(String op, Nodo n1, Nodo n2) {
    super("*");
    if (op.equals("*")) {

      figli.add(n1);
      figli.add(n2);
    } else {

      figli.add(n1);
      figli.add(new NodoPotenza("^", n2, new Razionale("-1")));
    }
  }

  // secondo costruttore per i programmi lineari

  public NodoMoltiplicazione(String op, List<Nodo> l) {
    super("*");
    if (op.equals("*")) {

      for (Nodo nodo : l) {
        figli.add(nodo);
      }
    } else {

      figli.add(l.get(0));
      l = l.subList(1, l.size());
      for (Nodo nodo : l) {
        figli.add((Nodo) new NodoPotenza("^", nodo, new Razionale("-1")));
      }
    }
  }

  // per stampare metodo default stampa nodo

  public List<Nodo> getFigli() {
    return figli;
  }

  protected void ordinaFigli() {
    Collections.sort(this.figli);
  }

  @Override
  public int compareTo(Nodo n) {
    if (n instanceof NodoMoltiplicazione) {
      return comparaFigliStessoTipo((NodoOperazione) n);
    } else if (n instanceof NodoAddizione) {
      return -1;
    }

    return 1; // moltiplicazione segue tutti tranne addizione
  }

  @Override
  public Nodo semplificaNodo() {
    List<Razionale> razionali = new ArrayList<>();
    // List<Variabile> variabili = new ArrayList<>();
    List<Nodo> nodi = new ArrayList<>();

    for (Nodo n : this.getFigli()) {
      if (n instanceof Razionale) {
        razionali.add((Razionale) n);
      }else if (n instanceof NodoMoltiplicazione) { // i fattori moltiplicativi diventano fattori della moltiplicazione più esterna
        for (Nodo nodo : ((NodoMoltiplicazione) n).getFigli()) {
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

    Razionale r = new Razionale(1, 1); // se r rimane uno lo omettiamo

    for (Razionale i : razionali) {
      r = r.Prodotto(i);
    }

   // Map<NodoOperazione, Razionale> potenze = new HashMap<>();
   Map<Nodo, Razionale> potenze = new HashMap<>();

   for (Nodo i : nodi) {
    if (!(i instanceof NodoPotenza)) { // rendo ogni nodo una potenza^1
      i = new NodoPotenza("^", i, new Razionale(1,1));
    }
    NodoOperazione n = (NodoOperazione) i;
   // NodoOperazione m = (NodoOperazione) n.getFigli().get(0);
    // NodoOperazione k = null;
    Nodo m =  n.getFigli().get(0);
    Nodo k = null;
    for (Nodo t : potenze.keySet()) {
      if (t.equals(m)) k = t;
    }
  
    if (k != null) { // se la potenza di base n è già presente, somma esponenti
      Razionale temp = potenze.get(k);
      temp = temp.Somma((Razionale) n.getFigli().get(1));
    
    potenze.put(k, temp);
    }else{ // se la potenza non è presente, la aggiungo
   
      potenze.put(m,  (Razionale) n.getFigli().get(1));
    }

   }

   List<Nodo> nuoviFigli = new ArrayList<>();
   if (!(r.equals(new Razionale(1, 1)))) nuoviFigli.add(r);
   if (r.equals(new Razionale(0, 1))) return new Razionale(0, 1);

   for (Map.Entry<Nodo, Razionale> p : potenze.entrySet()) {
    if (p.getValue().equals(new Razionale(1, 1))) {
      // se ha esponente 1 aggiungo solo il nodo
      nuoviFigli.add(p.getKey());
    }else if (p.getValue().equals(new Razionale(0, 1))){
      // se ha esponente 0 non aggiungo
      continue;
    }else{
      NodoPotenza temp = new NodoPotenza("^", p.getKey(), p.getValue());
      nuoviFigli.add(temp);
    }
   }

   if (r.equals(new Razionale(1, 1)) && nuoviFigli.size() == 0) return new Razionale(1, 1);
   if (nuoviFigli.size() == 0) return new Razionale(0, 0);
   if (nuoviFigli.size() == 1) {
    Nodo n = nuoviFigli.get(0);
    
    return n;
   }

   NodoMoltiplicazione ris = new NodoMoltiplicazione("*", nuoviFigli);
   ris.ordina();
    return ris;
  }

  // TODO prova della semplificaNodozione
  public static void main(String[] args) {
    //Nodo n = new NodoMoltiplicazione("*", List.of(new Razionale(2, 1), new Razionale(3, 1)));
    //Nodo n = new NodoMoltiplicazione("*", new NodoPotenza("^", new Variabile("x"), new Razionale(1, 2)), new NodoPotenza("^", new Variabile("x"), new Razionale(1, 1)));
    //Nodo n = new NodoMoltiplicazione("*", new NodoAddizione("+", new Variabile("x"), new Razionale(1, 1)), new NodoAddizione("+", new Variabile("x"), new Razionale(1, 1)));
   
    // Nodo n = new NodoMoltiplicazione("*", new NodoAddizione("+", new Variabile("x"), new Razionale(2, 1)), new NodoAddizione("+", new Variabile("x"), new Razionale(1, 1)));
    
 //   List<Nodo> l = List.of(new Variabile("x"), new Variabile("x"), new NodoMoltiplicazione("*", new Variabile("y"), new Variabile("y")));
  //List<Nodo> l = List.of( new NodoMoltiplicazione("*", new Razionale("2"), new NodoPotenza("^", new Variabile("x"), new Razionale(1, 2))), new NodoMoltiplicazione("*", new Razionale("2"), new NodoPotenza("^", new Variabile("x"), new Razionale(1, 3))));
   // Nodo n = new NodoMoltiplicazione("*", l);
   // Nodo n = new NodoMoltiplicazione("*", new NodoMoltiplicazione("*", new Razionale("1"), new Variabile("y")), new NodoMoltiplicazione("*", new Razionale("1"), new Variabile("y")));

    Nodo n = new NodoMoltiplicazione("*", new Razionale(1, 1), new Razionale(-2, 1));
   
  
    n = ((NodoMoltiplicazione) n).semplificaNodo();
    if (n instanceof Foglia) {
      System.out.println(n);
    }else{
      System.out.println(((NodoOperazione) n).stampaNodo());
    }

  }

}
