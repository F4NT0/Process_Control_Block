import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * =============================
 * SOLUÇÃO DA QUESTÃO 3 DA GII
 * Autor: Gabriel Fanto Stundner
 * =============================
 */

class ListaCriador extends Thread{
    private ArrayList<Integer> lista;
    private int min,max;
    
    public ListaCriador(int min,int max){
        this.min = min;
        this.max = max;
        lista = new ArrayList<>();
    }
    
    public ArrayList<Integer> getLista(){return lista;}

    
    public void run(){
        for(int i = min ; i < max ; i++){
            lista.add(i);
        }
    }

    // Método de Teste
    public void testeSaida(){
        for(int i = 0 ; i < lista.size() ; i++){
            if(lista.get(i) != null){
                System.out.println("Posição " + i + " : " + lista.get(i));
            }
        }
    }
}

class ListaBloqueio extends Thread{
    private Semaphore mutex;    
    ListaCriador lista1;
    ListaCriador lista2;
    
    public ListaBloqueio(ListaCriador lista1, ListaCriador lista2){
        this.mutex = new Semaphore(1);
        this.lista1 = lista1;
        this.lista2 = lista2;
    }

    /**
     * Adiciona o Item na Lista de Itens de entrada
     * @param lista
     * @param item
     */
    public void insere(ListaCriador lista, int item){
        if(lista.getLista().contains(item) == false){
            lista.getLista().add(item);    
        }
    }

    /**
     * Retira o elemento descobrindo seu index
     * @param lista
     * @param index
     * @return Integer
     */
    public int retira(ListaCriador lista, int value){
        int position = lista.getLista().indexOf(value);
        lista.getLista().remove(position);
        return value;
    }

    /**
     * Passa um valor do Final da Lista 1 para a Lista 2
     * @param lista1
     * @param lista2
     * @return boolean
     */
    public boolean passa(ListaCriador lista1, ListaCriador lista2){
        int position = lista2.getLista().size()-1;
        if(position >= 0){
            insere(lista1, lista2.getLista().get(position));
            retira(lista2, lista2.getLista().get(position));
            return true;
        }
        return false;
    }

    /**
     * Método para Rodar em Loop os Métodos
     * @param lista1
     * @param lista2
     */
    public void procLista(ListaCriador lista1, ListaCriador lista2){
      int aux = 10;
      while(aux > 0){
        passa(lista1, lista2);    
        aux--;
      }
        
    }  

    
    public void run(){
        try{
            mutex.acquire();
            procLista(lista1, lista2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            mutex.release();
        }
    }

    // Método de Teste
    public void teste(){
        for(int i = 0 ; i < lista1.getLista().size() ; i++){
            System.out.println("Lista 1: " + " Posicao " + i + " : " + lista1.getLista().get(i));
        }
        System.out.println("\n");
        for(int i = 0 ; i < lista2.getLista().size() ; i++){
            System.out.println("Lista 2: " + " Posicao " + i + " : " + lista2.getLista().get(i));
        }
        System.out.println("\n");
    }

}
public class Solution{
    public static void main(String[] args){
        // Listas Criadas 
        ListaCriador lista1 = new ListaCriador(0,3);
        ListaCriador lista2 = new ListaCriador(4,7);
        
        // Adicionando valores como Processo
        lista1.start();
        lista2.start();
        System.out.println("\nLista 1\n");
        lista1.testeSaida();
        System.out.println("\nLista 2\n");
        lista2.testeSaida();

        // Programa
        ListaBloqueio fase1 = new ListaBloqueio(lista1, lista2);
        ListaBloqueio fase2 = new ListaBloqueio(lista2, lista1);
        fase1.start();
        fase2.start();
        
        System.out.println("\nApós os Processos: \n");
        
        System.out.println("\nProcesso 1\n");
        fase1.teste();
        System.out.println("\nProcesso 2\n");
        fase2.teste();

        System.out.println("\n As Listas \n");
        System.out.println("\nLista 1\n");
        lista1.testeSaida();
        System.out.println("\nLista 2\n");
        lista2.testeSaida();
        
    }
}