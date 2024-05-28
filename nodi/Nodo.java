package luppolo.nodi;

public interface Nodo extends Comparable<Nodo> {

  int compareTo(Nodo n);

  void ordina(); // serve ai nodi per ordinarsi i figli sottostanti

  // public  Nodo espandi(){};
  // public  Nodo semplifica();
  // public  Nodo deriva();

}
