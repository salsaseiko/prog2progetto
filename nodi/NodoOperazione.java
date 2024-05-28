package luppolo.nodi;

import java.util.List;


public abstract class NodoOperazione implements Nodo {

  private String operazione;

  public NodoOperazione(String op) {
    operazione = op;
  }

  public String getOperazione() {
    return operazione;
  }
  ;

  public abstract List<Nodo> getFigli();

  protected abstract void ordinaFigli();

  public abstract Nodo semplificaNodo();

  public Nodo semplifica() {
    for (int i = 0; i < this.getFigli().size(); i++) {
      Nodo figlio = this.getFigli().get(i);
      if (figlio instanceof Foglia) {
      //  System.out.println(figlio);
        
        this.getFigli().set(i, ((Foglia) figlio).semplifica());
        figlio = ((Foglia) figlio).semplifica();
        System.out.println(figlio);
      }else{
        //System.out.println(((NodoOperazione) figlio).stampaNodo());
        this.getFigli().set(i, ((NodoOperazione)figlio).semplifica());
        figlio = ((NodoOperazione)figlio).semplifica();
        if (figlio instanceof Foglia) {
          System.out.println(figlio.toString());

        }else{
          System.out.println(((NodoOperazione) figlio).stampaNodo());

        }
      }


      
    }
    return this.semplificaNodo();
  }

  public String stampaNodo() {
    String s = getOperazione() + "(";

    List<Nodo> figli = getFigli();
    for (int i = 0; i < figli.size(); i++) {
      Nodo n = figli.get(i);
      if (n instanceof Foglia) {
        s += n.toString();
      } else {
        s += ((NodoOperazione) n).stampaNodo();
      }
      if (i != figli.size() - 1) {
        s += ", ";
      }
    }
    s += ")";
    return s;
  }

  @Override
  public String toString() {
    return stampaNodo();
  }

  @Override
  public void ordina() {
    for (Nodo figlio : getFigli()) {
      // ordino ciascun figlio
      figlio.ordina();
    }
    // poi ordino tutta la lista
    ordinaFigli();
  }

  // usato per comparare nodi dello stesso tipo, ex addizione - addizione
  // TODO perché protected?
  protected int comparaFigliStessoTipo(NodoOperazione n) {

    // TODO: questa parte lho tolta perché non serve stampare il nodo che ha meno figli per primo,
    // ma quello che ha figli "meno complicati"
    // ex. ho due figli allo stesso livello entrambi potenze: se uno dei due ha come base un
    // razionale mentre l'altro come base un altro nodo
    // stampiamo prima il razionale.

    // compara le grandezze dei nodi, ritorna -1 se this < n
    /*     int sizeComparison = Integer.compare(this.getFigli().size(), n.getFigli().size());
    if (sizeComparison != 0) {
        return sizeComparison;
    } */
    for (int i = 0; i < this.getFigli().size(); i++) {
      int comparison = this.getFigli().get(i).compareTo(n.getFigli().get(i));
      if (comparison != 0) {
        return comparison;
      }
    }
    return 0;
  }

    @Override
    public boolean equals(Object o) {
      if (o == this) return true;
    if (!(o instanceof NodoOperazione)) return false;
    NodoOperazione no = (NodoOperazione) o;
    if (this.operazione != no.operazione) {
      return false;
    }

    if (this.getFigli().size() != no.getFigli().size()) return false;
   
    this.ordina();
    no.ordina();


    for (int i = 0; i < this.getFigli().size(); i++) {
      if (!(this.getFigli().get(i).equals(no.getFigli().get(i)))) {
        
        return false; // appena trovo un figlio diverso, false
      }
    }
    return true;
    }


}
