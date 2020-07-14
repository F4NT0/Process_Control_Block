import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class Memory {
    private int size = 16;
    private List<Object> memory;

    public Memory() {
        memory = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            memory.add(i, null);
        }
    }

    public void teste() {
        for (int i = 0; i < memory.size(); i++) {
            System.out.println("Position " + i + " : " + memory.get(i));
        }
        System.out.println("\n");
    }

    public List<Object> getMemory() {
        return memory;
    }

    public int getMemSize() {
        return size+1;
    }
    
}

class ContextData {
    int r1, r2, r3, r4, r5, r6, r7, r8;
    int idProcess;

    public ContextData(int idProcess) {
        this.idProcess = idProcess;
    }

    public int getRegister(String register) {
        switch (register) {
            case "r1":
                return r1;
            case "r2":
                return r2;
            case "r3":
                return r3;
            case "r4":
                return r4;
            case "r5":
                return r5;
            case "r6":
                return r6;
            case "r7":
                return r7;
            case "r8":
                return r8;
        }
        return -1;
    }

    public void save(int r1, int r2, int r3, int r4, int r5, int r6, int r7, int r8) {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
        this.r7 = r7;
        this.r8 = r8;
    }
}

class Process {
    private int id;
    private String state;
    private int priority;
    private int pageNumbers = 4;
    private Map<Integer, Integer> pagination = new HashMap<>();
    int pc;

    public Process(int id, String state){
        this.id = id;this.state = state;
    }
    ContextData context = new ContextData(id);

    public int getId(){return id;}
    public int getPc(){return pc;}
    public String getState(){return state;}
    public int getPriority(){return priority;}
    public void setPc(int pc){this.pc = pc;}
    public void setState(String state){this.state = state;}
    public void setPriority(int priority){this.priority = priority;}

    // Paginação
    public Map<Integer,Integer> getPagination(){return pagination;}
    public void setPagination(Map<Integer,Integer> pages){this.pagination = pages;}
    public int getPageNumbers(){return pageNumbers;}
    
    // ContextData
    public void saveContextData(int r1,int r2, int r3, int r4, int r5, int r6, int r7, int r8){
        context.save(r1, r2, r3, r4, r5, r6, r7, r8);
    }
    public int getRegister(String register){
        return context.getRegister(register);
    }

}

class PCB{
    Queue<Process> queue;
    Memory mem;
    int r1,r2,r3,r4,r5,r6,r7,r8;
    Map<Integer,Integer> pagination = new HashMap<>();

    public PCB(Memory mem){
        queue = new LinkedList<>();
        this.mem = mem;
        mem.getMemory().add(0,queue);    
    }

    public void createProcess(int id){
        Process process = new Process(id,"NEW");
        process.setPriority(queue.size());
        
        // Verificando Frames Livres
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < mem.getMemSize() ; i++){
            if(mem.getMemory().get(i) == null){
                list.add(i);
            }
        }
        // Pegando os primeiros Frames para o Processo
        Map<Integer, Integer> auxMap = new HashMap<>();
        for(int i = 0 ; i < process.getPageNumbers() ; i++){
            auxMap.put(i, list.get(i));// (página,frame)
        }

        // Adicionando os valores de exemplo
        for(Integer page : auxMap.keySet()){
            Integer frame = auxMap.get(page);
            insertOnMemory(page, frame, process.getId());
        }

        process.setPagination(auxMap);
        queue.add(process);
    }

    public void insertOnMemory(int page, int frame, int id){
        String[] values = new String[4];
        for(int i = 0 ; i < values.length ; i++){
            values[i] = "Página " + page + " Frame " + frame + " Processo " + id + " Linha " + i;
        }
        mem.getMemory().set(frame,values);
    }

    // Leitura de uma Página
    private int frameLocation(int relative, int frameSize){
        return relative / frameSize;
    }
    
    private int offset(int value, int frameSize){
       return value - frameSize;
    }

    public void lerLinha(int valor){
        Process process = queue.poll();
        int discoverFrame = frameLocation(valor, process.getPageNumbers());
        int offset = offset(valor, process.getPageNumbers());
        int accessPosition = process.getPagination().get(discoverFrame); //frame que iremos acessar
        String[] vetorAux = (String[]) mem.getMemory().get(accessPosition);
        System.out.println("Valor na Linha " + offset + " da Posição " + accessPosition + " : " + vetorAux[offset]);
        process.saveContextData(offset, r2, r3, r4, r5, r6, r7, r8);
        queue.add(process);
    }

    // TESTES
    public int queueSize(){return queue.size();}
    public void testeFila(){
       while(queue.size() > 0){System.out.println("Processo " + queue.poll().getId());}
    }
    public void testeRegisters(){
        Process aux = queue.poll();
        System.out.println("Processo " + aux.getId());
        Map<Integer,Integer> pages = aux.getPagination();
        for(Integer key : pages.keySet()){Integer value = pages.get(key); System.out.println("Página: " + key + " | Frame: " + value);}
        System.out.println("Prioridade: " + aux.getPriority());
    }
    public void testeContextData(){
        Process aux = queue.poll();
        System.out.println("Processo: " + aux.getId());
        System.out.println("ContextData: " + aux.getRegister("r1") + "\n");
    }

    public void testeVetores(int position){
        String[] aux = (String[]) mem.getMemory().get(position);
        for(int i  = 0 ; i < aux.length ; i++){
            System.out.println("Valores da Posição " + position + " : " + aux[i]);
        }
        System.out.println("\n");
    }

}

public class Solution{
    public static void main(String[] args){
        Memory mem = new Memory();
        PCB p = new PCB(mem);
        
        p.createProcess(0);
        p.createProcess(1);
        p.createProcess(2);
        p.createProcess(3);
        mem.teste();
        
        //p.testeRegisters();
        //p.testeRegisters();
        //p.testeRegisters();

        //p.testeVetores(1);
        p.testeVetores(2);
        //p.testeVetores(3);
        //p.testeVetores(4);
        // p.testeVetores(5);
        // p.testeVetores(6);
        // p.testeVetores(7);
        // p.testeVetores(8);
        
        p.lerLinha(4);
        System.out.println("\n");
        System.out.println("Teste do ContextData ");
        p.testeContextData();



        

    }
}