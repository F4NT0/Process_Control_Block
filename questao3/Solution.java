import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
* Suponha que o algoritmo concorrente abaixo, que tem situações em que insere
    elementos nas listas A e B, retira elementos destas listas, e também passa elementos
    de uma lista para outra de forma atômica. Ou seja, o elemento transferido é observável
    em A ou em B. A operação passa não permite que outro processo observe o estado
    em que o elemento foi retirado de uma lista e ainda não inserido na outra. Como as
    listas A e B são acessadas concorrentemente, este algoritmo usa semáforos para
    proteger o acesso às operações de inserção e retirada de cada lista. Uma estrutura
    agregando uma lista e um semáforo para cuidar de sua exclusão mútua é criada, como
    abaixo.
*/

class ListaCriador{
    private ArrayList<Integer> lista;
    public ListaCriador(){
        lista = new ArrayList<>();
    }
    public ArrayList<Integer> getLista(){return lista;}
}

class ListaBloqueio extends Thread{
    private Semaphore mutex;    

    public ListaBloqueio(){
        this.mutex = new Semaphore(1);
    }

    public Semaphore getMutex(){return mutex;}

    // Métodos de interação

    /**
     * Adiciona o Item na Lista de Itens de entrada
     * @param lista
     * @param item
     */
    public void insere(ArrayList<Integer> lista, int item){
        lista.add(item);
    }

    /**
     * Retira, Remove e retorna o Conteudo de uma Posição
     * @param lista
     * @param index
     * @return Integer
     */
    public int retira(ArrayList<Integer> lista, int index){ // Adicionado o index no Método
        int aux = lista.get(index);
        lista.remove(index);
        return aux;
    }

    /**
     * Passa um valor aleatório da Lista 1 para a Lista 2
     * @param lista1
     * @param lista2
     * @return boolean
     */
    public boolean passa(ArrayList<Integer> lista1, ArrayList<Integer> lista2){
        if(lista1.size() > 0){
            int posAleatoria = (int) ((Math.random() * ((lista1.size() - 0) + 1)) + 0);
            lista2.add(lista1.get(posAleatoria));
            return true;
        }
        return false;
    }

    public void run(){ // FALTA FINALIZAR ESSA FUNÇÃO
        try{
            mutex.acquire();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            mutex.release();
        }
    }

    // TODO: TEM QUE CRIAR UM MÉTODO QUE ADICIONE VALORES NAS LISTAS
    // TODO: TEM QUE POSSUI A FUNÇÃO PROCLISTA(OLHAR NO MATERIAL DA GII)
    // TODO: TEM QUE FAZER FUNCIONAR

}