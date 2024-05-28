package luppolo.nodi;

public class Razionale implements Foglia {

  /** Campo numeratore della frazione */
  private int numeratore;

  private int denominatore;

  public Razionale(String v) {
    if (v.contains("/")) {
      String[] split = v.split("/");
      int n = Integer.valueOf(split[0]);
      int d = Integer.valueOf(split[1]);

      // metto sempre il segno al numeratore
      denominatore = Math.abs(d);

      if ((n >= 0 && d >= 0) || (n < 0 && d < 0)) {
        numeratore = Math.abs(n);
      } else {
        numeratore = Math.abs(n) * -1;
      }

    } else {
      numeratore = Integer.valueOf(v);
      denominatore = 1;
    }
  }

  public Razionale(int n, int d) {
    if (d != 0) {

      denominatore = Math.abs(d);

      if ((n >= 0 && d >= 0) || (n < 0 && d < 0)) {
        numeratore = Math.abs(n);
      } else {

        numeratore = Math.abs(n) * -1;
      }

    } else {
      throw new IllegalArgumentException("Il denominatore non può essere 0");
    }
  }

  /**
   * Restituisce la frazione ridotta ai minimi termini del parametro f
   *
   * @param f frazione da ridurre
   * @return frazione ridotta
   */
  public Razionale MinimiTermini() {
    int a, b;
    int mcd = MCD(Math.abs(this.numeratore), this.denominatore);

    a = this.numeratore / mcd;
    b = this.denominatore / mcd;
    // System.out.println("miao4");
    return new Razionale(a, b);
  }

  /**
   * Funzione per calcolare la somma tra due frazioni, restituisce la somma ridotta ai minimi
   * termini
   *
   * @param f secondo addendo, this è il primo
   * @return somma ridotta ai minimi termini
   */
  public Razionale Somma(Razionale f) {

    int mcm = mcm(this.denominatore, f.denominatore);

    int a = 0;
    int b = 0;
    a = (mcm / this.denominatore) * this.numeratore;
    b = (mcm / f.denominatore) * f.numeratore;

    int temp = a + b;

    Razionale somma = new Razionale(temp, mcm);

    return somma.MinimiTermini();
  }

  /**
   * Funzione per calcolare il prodotto di due frazioni
   *
   * @param f moltiplicatore, il moltiplicando è this
   * @return prodotto ridotto ai minimi termini
   */
  public Razionale Prodotto(Razionale f) {
    int nuovoNum = this.numeratore * f.numeratore;
    int nuovoDen = this.denominatore * f.denominatore;
    Razionale nf = new Razionale(nuovoNum, nuovoDen);
    return nf.MinimiTermini();
  }

  /* eleva il razionale al numeratore della potenza */
  public Razionale Potenza(Razionale e) {
    int n;
    int d;

    if (e.èNegativo()) {

      // inverto la frazione così sistema anche il segno
      Razionale inverso = new Razionale(this.denominatore, this.numeratore);

      n = inverso.numeratore;
      d = inverso.denominatore;

      // lo trasformo in positivo
      e = new Razionale(e.numeratore, -1);

    } else {
      n = this.numeratore;
      d = this.denominatore;
    }

    n = (int) Math.pow(n, e.numeratore);

    d = (int) Math.pow(d, e.numeratore);

    Razionale nf = new Razionale(n, d);

    if (MCD(n, d) == 1) {
      return nf;
    }
    // TODO vediamo se funge, mi dava problemi di tempistiche
    return nf.MinimiTermini(); 
  }


  // TODO da provare
  public Nodo SemplificaPotenza(Razionale e) {
    double n = 0;
    double d= 0;
    Nodo nodoN = null;
    Nodo nodoD = null;
    boolean nRazionale = true;
    boolean dRazionale = true;

    Nodo ris = null;

    if (this.numeratore < 0) { // math.pow non gestisce i negativi
      n = Math.pow(-this.numeratore, 1.0/e.denominatore);
      n = -n;
    }else{
      n = Math.pow(this.numeratore, 1.0/e.denominatore);
    }
    
    if (!(èIntero(n))) {
      
     // nodoN = new NodoPotenza("^", new Razionale(this.numeratore, 1).Potenza(e), new Razionale(1, e.denominatore));
      nRazionale = false;
    }

    d = Math.pow(this.denominatore, 1.0/e.denominatore);

    
    if (!(èIntero(d))) {
      // -1 perché stava al denominatore
     // nodoD = new NodoPotenza("^", new Razionale(this.denominatore, 1).Potenza(e), new Razionale(-1, e.denominatore));
      dRazionale = false;
    }

     // indistintamente eleviamo alla potenza, tanto Potenza(e) eleva solo al e.numeratore
    if (nRazionale && dRazionale) {
      ris = new Razionale((int)n, (int)d).Potenza(e);
      
    }else if (nRazionale) {
      if (e.numeratore == -1) {
        // TODO non metto il -1 perché tanto rivolta la frazione
        // siccome la frazione è rivoltata ed il segno va al numeratore, che è ora nodoD, scambiamo i segni
        if (n < 0) {
          n = -n;
          this.denominatore = -this.denominatore;
        }
      
        nodoD = new NodoPotenza("^", new Razionale(this.denominatore, 1), new Razionale(1, e.denominatore));
      }else{
        nodoD = new NodoPotenza("^", new Razionale(this.denominatore, 1).Potenza(e), new Razionale(-1, e.denominatore));

      }
      if (n != 1) {
        ris = new NodoMoltiplicazione("*", new Razionale((int)n, 1).Potenza(e), nodoD);
            }else{
        ris = nodoD;
      }
      
    }else if (dRazionale) {

      if (e.numeratore == -1) {

// se si rivolta la frazione, il meno del numeratore va al nuovo numeratore (il denominatore)

        if (this.numeratore < 0) {
          this.numeratore = -this.numeratore;
          d = -d;
        }
        nodoN = new NodoPotenza("^", new Razionale(this.numeratore, 1), new Razionale(-1, e.denominatore));
        
      }else{
        nodoN = new NodoPotenza("^", new Razionale(this.numeratore, 1).Potenza(e), new Razionale(1, e.denominatore));

      }
       
      //nodoN = new NodoPotenza("^", new Razionale(this.numeratore, 1).Potenza(e), new Razionale(1, e.denominatore));
      if (d != 1) {
        ris = new NodoMoltiplicazione("*", new Razionale(1, (int)d).Potenza(e), nodoN);
      }else{
        ris = nodoN;
      }
      
    }else{

      // se nessuno dei due è razionale lascio la potenza così comé
      //ris = new NodoMoltiplicazione("*",nodoN, nodoD);

      ris = new NodoPotenza("^", this, e);

      // TODO non vuole che una potenza 3/8 ^ 2/3 sia espressa come (2*3^1/3)/4 ma come (9^1/3)/4
     /*  if (e.numeratore != 1) {
        ris = new NodoPotenza("^", ris, new Razionale(e.numeratore, 1));
      }
    */
    }

    return ris;
  }

  public boolean èNegativo() {
    if (this.numeratore < 0) {
      return true;
    }
    return false;
  }



  public boolean èFrazione() {
    if (this.denominatore != 1) {
      return true;
    }
    return false;
  }

  private boolean èIntero(double n) {
    return n % 1 == 0;
  }


  /**
   * Funzione per calcolare dati due numeri il loro MCD
   *
   * @param a intero 1
   * @param b intero 2
   * @return MCD(a, b)
   */
  private int MCD(int a, int b) {
    int mcd = 0;
    int max;
    if (a > b) {
      max = a;
    } else max = b;

    for (int i = max; i >= 1; i--) {
      if (a % i == 0 && b % i == 0) {
        mcd = i;
        break;
      }
    }
    return mcd;
  }

  /**
   * Funzione per calcolare dati due numeri il loro mcm
   *
   * @param a intero 1
   * @param b intero 2
   * @return mcm(a, b)
   */
  private int mcm(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);
    int max;
    if (a > b) {
      max = a;
    } else max = b;
    int mcm = max;
    for (int i = max; i <= a * b; i++) {
      if (i % a == 0 && i % b == 0) {
        mcm = i;
        break;
      }
    }

    return mcm;
  }

  public int getNumeratore() {
    return numeratore;
  }
  public int getDenominatore() {
    return denominatore;
  }

  @Override
  public String toString() {
    if (denominatore == 1) {
      return String.valueOf(numeratore);
    }
    return String.valueOf(numeratore) + "/" + String.valueOf(denominatore);
  }

  @Override
  public int compareTo(Nodo n) {

    if (n instanceof Razionale) {
      // compariamo le frazioni
      int a = this.numeratore * ((Razionale) n).denominatore;
      int b = ((Razionale) n).numeratore * this.denominatore;
      return Integer.compare(a, b);
    }
    return -1; // infatti nessuno è prima di Razionale
  }

  @Override
  public void ordina() {
    // una foglia non ordina nulla al di sotto
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Razionale)) return false;
    Razionale r = ((Razionale) o).MinimiTermini();
    this.MinimiTermini();
    if (r.numeratore == this.numeratore && r.denominatore == this.denominatore) {
      return true;
    }
    return false;
  }

  @Override
  public Nodo semplifica() {
    return this;
  }

  
  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

}
