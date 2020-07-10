import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Memory {
    private int size = 32;
    private List<Object> memory;
    public Memory(){
        memory = new ArrayList<>();
        for(int i = 0 ; i < size ; i++){memory.add(i,null);}
    }

    public void teste(){
        for(int i = 0 ; i < memory.size() ; i++){
            System.out.println("Position " + i + " : " + memory.get(i));
        }
    }
    public List<Object> getMemory(){return memory;}
}

class Process{
    private int id; // Identificador Unico do Processo
    private String state; // Estado do Processo
    private int priority; // Prioridade do Processo
    int baseRegister; // Primeira Posição do Processo
    int boundsRegister; // Ultima Posição do Processo
    int pc; //Valor do Program Counter

    public Process(int id, String state){
        this.id = id;this.state = state;
    }

    public int getId(){return id;}
    public int getPc(){return pc;}
    public String getState(){return state;}
    public int getPriority(){return priority;}
    public int getBaseRegister(){return baseRegister;}
    public int getBoundsRegister(){return boundsRegister;}
    public void setPc(int pc){this.pc = pc;}
    public void setState(String state){this.state = state;}
    public void setPriority(int priority){this.priority = priority;}
}

class PCB{
    Queue<Process> queue;
    public PCB(Memory mem){
        queue = new LinkedList<>();
        mem.getMemory().add(queue);
        

    
    }

    public void createProcess(int id){
        Process process = new Process(id,"NEW");
        process.setPriority(queue.size());
        queue.add(process);
        
        
    }
    public int queueSize(){return queue.size();}
    public void testeFila(){
       while(queue.size() > 0){
        System.out.println("Processo " + queue.poll().getId()); 
       }
    }
    
    
}

public class Solution{
    public static void main(String[] args){
        Memory mem = new Memory();
        PCB p = new PCB(mem);
        p.createProcess(4);
        p.createProcess(2);
        p.createProcess(1);
        p.createProcess(3);
        p.queueSize();
        p.testeFila();
        
    }
}