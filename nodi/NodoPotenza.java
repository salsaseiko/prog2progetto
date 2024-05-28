package luppolo.nodi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodoPotenza extends NodoOperazione {

  private List<Nodo> figli = new ArrayList<>();

  /* voglio che una potenza sia solo espressa da una radice ^ e due figli esponente e base
   * per questo se arrivano più esponenti, li calcolo
   */
  /*
   * le potenze possono solo essere b^e, non b^nodo^nodo ecc...
   * non posso avere b^(/(1, 2)), devo avere b^(1/2)
   */

  public NodoPotenza(String op, Nodo n1, Nodo n2) {

    super("^");
    figli.add(n1);
    if (!(n2 instanceof Razionale)) {
      // Razionale nuovo_n2 = new Razionale(n2);
      Razionale nuovo_n2 = calcola(n2);
      figli.add(nuovo_n2);
    } else {
      figli.add(n2);
    }
  }

  public NodoPotenza(String op, List<Nodo> l) {
    super("^");
    figli.add(l.get(0)); // base

    List<Razionale> trovaEsponente = new ArrayList<>(); // troviamo unico esponente
    for (int i = 1; i < l.size(); i++) {
      if (!(l.get(i) instanceof Razionale)) {
        Razionale nuovo_r = calcola(l.get(i));

        trovaEsponente.add(nuovo_r);
      } else {
        trovaEsponente.add((Razionale) l.get(i));
      }
    }
    Razionale ris = trovaEsponente.get(0);
    for (int i = 1; i < trovaEsponente.size(); i++) {
      NodoPotenza temp = new NodoPotenza("^", ris, trovaEsponente.get(i));
      ris = calcola(temp);
    }
    figli.add(ris);
  }

  public static Razionale calcola(Nodo n) {

    if (n instanceof NodoOperazione) {
      String op = ((NodoOperazione) n).getOperazione();
      List<Razionale> operandi = new ArrayList<>();

      for (Nodo i : ((NodoOperazione) n).getFigli()) {

        if (i instanceof Razionale) {
          operandi.add((Razionale) i);
        } else {
          // Aggiorna correttamente la lista degli operandi con il risultato ricorsivo

          operandi.add(calcola(i));
        }
      }

      return operazione(op, operandi);
    } else if (n instanceof Razionale) {
      return (Razionale) n;
    }
    return null;
  }

  public static Razionale operazione(String op, List<Razionale> operandi) {

    Razionale ris = operandi.get(0);

    operandi = operandi.subList(1, operandi.size());
    switch (op) {
      case "+":
        for (Razionale r : operandi) {
          ris = ris.Somma(r);
        }
        break;
      case "*":
        for (Razionale r : operandi) {
          ris = ris.Prodotto(r);
        }
        break;
      case "^":
        for (Razionale r : operandi) {

          ris = ris.Potenza(r);
        }
        break;
      default:
        break;
    }

    return ris;
  }

  /*public NodoPotenza(String op, List<Nodo> l) {
      operazione = op;
      figli.add(l.get(0)); // primo figlio = base
      if (l.size() > 2) {
          Razionale esponente = calcola(l.get(1), l.get(2));
          int i = 2;
          while (i < l.size()) {
              esponente = calcola(new Foglia(esponente), l.get(i));
          }

      }else{
          figli.add(l.get(1));
      }

  }

  public Razionale calcola(Nodo b, Nodo e) {



  }*/

  public List<Nodo> getFigli() {
    return figli;
  }

  protected void ordinaFigli() {
    Collections.sort(this.figli);
  }

  @Override
  public void ordina() {
    for (Nodo n : figli) {
      n.ordina();
    }

    // override di ordina perché vogliamo solo che si ordinino i suoi figli
    // invece la lista di una potenza non deve essere ordinata:
    // base, esponente (non devono invertirsi)
  }

  @Override
  public int compareTo(Nodo n) {
    if (n instanceof NodoPotenza) {
      return comparaFigliStessoTipo((NodoOperazione) n);
    } else if (n instanceof NodoAddizione || n instanceof NodoMoltiplicazione) {
      return -1;
    }
    return 1; // prima di add e mul, dopo foglie
  }

  @Override
  public Nodo semplificaNodo() {
    // si sottointende che tutto ciò che era da semplificare è stato fatto prima, ad es i razionali
    Nodo base = this.getFigli().get(0);
    Razionale esponente = (Razionale) this.getFigli().get(1); // per come ho definito, una potenza ha e = razionale..
    if (esponente.getNumeratore() == 0) {
      return new Razionale(1, 1);
    }else if (esponente.getNumeratore() == 1 && esponente.getDenominatore() == 1) {
      return base;
    }
    if (base instanceof Razionale) {
      if (((Razionale) base).getNumeratore() == 0) {
        return new Razionale(0, 1);
      }
      return ((Razionale) base).SemplificaPotenza(esponente);
    }

    return this; // non posso semplificarlo ulteriormente

  }

  // TODO prova potenza semplifica

  public static void main(String[] args) {
    NodoPotenza n = new NodoPotenza("^", new Razionale(3, 7), new Razionale(2, 3));
    Nodo ris = n.semplificaNodo();
    if (ris instanceof Razionale) {
      System.out.println(ris.toString());
    }else{
      System.out.println(((NodoOperazione) ris).stampaNodo());
    }
  }


}
