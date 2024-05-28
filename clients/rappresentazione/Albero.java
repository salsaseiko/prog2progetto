package clients.rappresentazione;

import java.util.Scanner;
import luppolo.Espressione;

/** Classe contenete il client per verificare la rappresentazione ad albero delle espressioni. */
public class Albero {

  /**
   * Client per verificare la rappresentazione di un'espressione come disegno testuale dell'albero.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard corrispondenti ad un programma
   * lineare, costruisce l'espressione corrispondente e ne emette la rappresentazione come disegno
   * testuale dell'albero nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      while (s.hasNextLine()) {
        Espressione e = new Espressione(s.nextLine());
        e.stampaAlbero();
        System.out.println();
      }
    }
  }
}
