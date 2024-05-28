package luppolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import luppolo.nodi.Foglia;
import luppolo.nodi.Nodo;
import luppolo.nodi.NodoAddizione;
import luppolo.nodi.NodoMoltiplicazione;
import luppolo.nodi.NodoOperazione;
import luppolo.nodi.NodoPotenza;
import luppolo.nodi.Razionale;
import luppolo.nodi.Variabile;

public class Espressione {

  private NodoOperazione radice;

  /* espressione = salvo solo il primo nodo
   *
   * costruisco diversamente a seconda che a leggerla sia in notazione polacca o in lineare
   * per distinguere, guardo se il primo carattere == .
   */
  public Espressione(String s) {
    if (s.charAt(0) == '.') {
      costruisciDaProgrammaLineare(s);
    } else {
      costruisciDaNotazionePolacca(s);
    }

    radice.ordina();
  }

  private void costruisciDaNotazionePolacca(String str) {
    Stack<Nodo> stack = new Stack<>();

    String[] s = str.split("\\s+"); // TODO espressione regolare che indica uno o più spazi. trovata su chatgpt

    for (int i = s.length - 1; i >= 0; i--) {

      String el = s[i];

      if (èFoglia((el))) {

        // se ho un numero seguito da uno spazio, metto tutto nella stessa foglia: è un negativo,
        // ovvero non faccio che - diventa operazione
        Foglia f = null;
        if (èRazionale(el)) {
          f = new Razionale(el);
        } else {
          f = new Variabile(el);
        }

        stack.push(f);

        System.out.println("PUSH " + f.toString());
        System.out.println("dimensione stack " + stack.capacity());

      } else {
        NodoOperazione n = null;
        Nodo a = stack.pop();
        Nodo b = stack.pop();
        System.out.println(a.toString() + el + b.toString());
        if (el.equals("+") || el.equals("-")) {

          n = new NodoAddizione(el, a, b);

        } else if (el.equals("*") || el.equals("/")) {
          n = new NodoMoltiplicazione(el, a, b);
        } else {
          n = new NodoPotenza(el, a, b);
        }
        System.out.println("PUSH " + n.stampaNodo());
        stack.push(n);
      }
    }

    radice = (NodoOperazione) stack.pop(); // ultimo nodo =  radice
  }

  private void costruisciDaProgrammaLineare(String s) {
    String[] comandi = s.split("\n");

    Nodo[] nodi = new Nodo[comandi.length];

    int i = 0;
    for (String c : comandi) {
      //  c = c.replaceAll(" ", "");
      String[] comando = c.split(" ");
      // if (c.charAt(0) == '.') {
      if (comando[0].equals(".")) {
        Foglia f = null;
        if (èRazionale(comando[1])) {
          //  if (èRazionale(c.substring(1))) { //togli .
          //  f = new Razionale(c.substring(1));
          f = new Razionale(comando[1]);
        } else {
          // f = new Variabile(c.substring(1));
          f = new Variabile(comando[1]);
        }
        nodi[i] = f;

      } else {
        NodoOperazione n = null;
        // String op = String.valueOf(c.charAt(0));
        //  c = c.substring(1);
        String op = comando[0];

        //  Nodo n1 =  nodi[Integer.parseInt(String.valueOf(c.charAt(0)))];
        // Nodo n2 = null;

        List<Nodo> l = new ArrayList<>();

        for (int j = 1; j < comando.length; j++) { // parte da 1 perché salta l'operazione
          // l.add(nodi[Integer.parseInt(String.valueOf(c.charAt(j)))]);
          l.add(nodi[Integer.parseInt(comando[j])]);
        }

        switch (op) {
          case "-":
            // fallthrough: non mettendo break cade al caso di sotto
          case "+":
            n = new NodoAddizione(op, l);
            break;
          case "/":

          case "*":
            n = new NodoMoltiplicazione(op, l);
            break;
          case "^":
            n = new NodoPotenza(op, l);
            break;
          default:
            break;
        }
        nodi[i] = n;
      }
      i++;
    }

    radice = (NodoOperazione) nodi[nodi.length - 1];
  }

  /*  private Nodo ricorsioneProgrammaLineare(String op, String c, Nodo[] nodi) {
          NodoOperazione n = null;

        //  Nodo n1 =  nodi[Integer.parseInt(String.valueOf(c.charAt(0)))];
         // Nodo n2 = null;

         List<Nodo> l = new ArrayList<>();

         for (int j = 0; j < c.length(); j++) {
              l.add(nodi[Integer.parseInt(String.valueOf(c.charAt(0)))]);
         }
         switch (op) {
          case "-":
              // fallthrough: non mettendo break cade al caso di sotto
          case "+":
          n = new NodoAddizione(op, l);
              break;
          case "/":

          case "*":
          n = new NodoMoltiplicazione(op, l);
              break;
          case "^":
        //  n = new NodoPotenza(op, l);
              break;
          default:
              break;
      }
  /*
          if (c.length() > 2) {
              n2 = ricorsioneProgrammaLineare(op, c.substring(1), comandi, nodi, i);
          }else{
              n2 = nodi[Integer.parseInt(String.valueOf(c.charAt(1)))];
          }

                  switch (op) {
                      case "-":
                          // fallthrough: non mettendo break cade al caso di sotto
                      case "+":
                      n = new NodoAddizione(op, n1, n2);
                          break;
                      case "/":

                      case "*":
                      n = new NodoMoltiplicazione(op, n1, n2);
                          break;
                      case "^":
                      n = new NodoPotenza(op, n1, n2);
                          break;
                      default:
                          break;
                  }
          return n;

      }*/

  public boolean èFoglia(String c) {
    if (!(c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^"))) {
      return true;
    }
    return false;
  }

  public boolean èRazionale(String c) {
    try {
      Integer.valueOf(c);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return radice.stampaNodo();
  }

  public void stampaAlbero() {
    System.out.println(radice.getOperazione());
    recursiveTree("", radice.getFigli());
  }

  private void recursiveTree(String pre, List<Nodo> nodi) {
    for (int i = 0; i < nodi.size(); i++) {
      boolean last = (i == nodi.size() - 1);

      // String prefisso = pre + (isLast ? "    " : "│   ");
      String prefisso = pre + (last ? "    " : "│   ");
      if (nodi.get(i) instanceof Foglia) {
        Foglia f = (Foglia) nodi.get(i);
        System.out.println(pre + (last ? "╰── " : "├── ") + f.toString());
      } else {
        NodoOperazione n = (NodoOperazione) nodi.get(i);
        System.out.println(pre + (last ? "╰── " : "├── ") + n.getOperazione());
        recursiveTree(prefisso, n.getFigli());
      }
    }
  }

  public NodoOperazione getRadice() {
    return radice;
  }


  public Nodo semplifica() {

   
    return radice.semplifica();
   
  }

}
