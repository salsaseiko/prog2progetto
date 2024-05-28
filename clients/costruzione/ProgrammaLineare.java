package clients.costruzione;

import java.util.Scanner;
import luppolo.Espressione;

/**
 * Classe contenete il client per verificare la costruzione di un'espressione a partire da un
 * programma lineare.
 */
public class ProgrammaLineare {

  /**
   * Client per verificare la costruzione di un'espressione a partire da un programma lineare.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard corrispondenti ad un programma
   * lineare, costruisce l'espressione corrispondente e ne emette la rappresentazione linearizzata
   * nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) {
    String input = "";
    try (Scanner s = new Scanner(System.in)) {
      while (s.hasNextLine()) {
        input += s.nextLine() + "\n";
      }
    }

    Espressione e = new Espressione(input);
    System.out.println(e.toString());
  }
}
