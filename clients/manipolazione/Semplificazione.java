package clients.manipolazione;

import java.util.Scanner;

import luppolo.Espressione;
import luppolo.nodi.Foglia;
import luppolo.nodi.Nodo;
import luppolo.nodi.NodoOperazione;

/** Classe contenete il client per verificare la semplificazione delle espressioni. */
public class Semplificazione {

  /**
   * Client per verificare la semplificazione delle espressioni.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
   * rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
   * corrispondente, la <em>semplifica</em> e emette la rappresentazione linearizzata
   * dell'espressione così ottenuta nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  
   
  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      while (s.hasNextLine()) {
        Espressione e = new Espressione(s.nextLine());
        //System.out.println(e);
      //   e.stampaAlbero();
        Nodo n = e.semplifica();
        if (n instanceof Foglia) {
          System.out.println(n.toString());
        }else{
          System.out.println(((NodoOperazione) n).stampaNodo());
        }
        
      }
    }
   }

}
