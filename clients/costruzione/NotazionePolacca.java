package clients.costruzione;

import java.util.Scanner;
import luppolo.Espressione;

/**
 * Classe contenete il client per verificare la costruzione di un'espressione a partire dalla
 * notazione polacca.
 */
public class NotazionePolacca {

  /**
   * Client per verificare la costruzione di un'espressione a partire dalla notazione polacca.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
   * rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
   * corrispondente e ne emette la rappresentazione linearizzata nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) {

    try (Scanner s = new Scanner(System.in)) {
      while (s.hasNextLine()) {
        Espressione e = new Espressione(s.nextLine());
        System.out.println(e.toString());
      }
    }
  }
}
