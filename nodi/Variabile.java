package luppolo.nodi;

public class Variabile implements Foglia {

  private String valore;

  public Variabile(String v) {
    valore = v;
  }

  @Override
  public String toString() {
    return valore;
  }

  @Override
  public int compareTo(Nodo n) {
    if (n instanceof Razionale) {
      return 1;
    } else if (n instanceof Variabile) {
      return this.valore.compareTo(((Variabile) n).valore);
    }
    return -1; // precede tutto tranne Razionale
  }

  @Override
  public void ordina() {
    // una foglia non ordina nulla al di sotto
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Variabile)) return false;
    Variabile v = ((Variabile) o);
    return this.valore.equals(v.valore);
  }

  @Override
  public int hashCode() {
    return valore.hashCode();
  }

  @Override
  public Nodo semplifica() {
    return this;
  }

}
