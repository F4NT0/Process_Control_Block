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
    public int getMemSize(){return size;}

    // Só para definir uma memoria particionada
    public int getPartition(int numberProcess){
        switch(numberProcess){
            case 1: return 8;
            case 2: return 16;
            case 3: return 24;
            case 4: return 32;
        }
        return -1;
    }
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
    public void setBaseRegister(int value){baseRegister = value;}
    public int getBoundsRegister(){return boundsRegister;}
    public void setBoundsRegister(int value){boundsRegister = value;}
    public void setPc(int pc){this.pc = pc;}
    public void setState(String state){this.state = state;}
    public void setPriority(int priority){this.priority = priority;}
}

class PCB{
    Queue<Process> queue;
    Memory mem;
    public PCB(Memory mem){
        queue = new LinkedList<>();
        this.mem = mem;
        mem.getMemory().add(0,queue);    
    }

    public void createProcess(int id){
        Process process = new Process(id,"NEW");
        process.setPriority(queue.size());
        process.setBoundsRegister(mem.getPartition(id));
        process.setBaseRegister(mem.getPartition(id)-7); // 7 porque são 8 posições por part.
        process.setPc(process.getBaseRegister());
        insertOnMemory(process.getBaseRegister(), process.getBoundsRegister(), id);
        queue.add(process);
    }

    public void insertOnMemory(int registerBase, int boundsRegister, int id){
        int j = 0 ;
        for(int i = registerBase ; i <= boundsRegister ; i++){
            String value = "Lendo Linha " + j + " Processo " + id;
            j++;
            mem.getMemory().set(i,value);
        }
    }

    private int adder(int relative, int baseReg){
        return relative + baseReg;
    }
    private boolean comparator(int value, int boundsReg){
        if(value <= boundsReg){
            return true;
        }
        return false;
    }

    public void lerLinha(int valor){
        Process process = queue.poll();
        int value = adder(valor, process.baseRegister);
        if(comparator(value, process.boundsRegister)){
            System.out.println("Posição " + value + " | Processo " + process.getId() + " : " + mem.getMemory().get(value));
        }else{
            System.out.println("Posição " + value + " Não existe no Processo " + process.getId());
        }
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
        System.out.println("Base Register: " + aux.getBaseRegister());
        System.out.println("Bounds Register: " + aux.getBoundsRegister());
        System.out.println("Prioridade: " + aux.getPriority());
    }

}

public class Solution{
    public static void main(String[] args){
        Memory mem = new Memory();
        PCB p = new PCB(mem);

        p.createProcess(1);
        p.createProcess(2);
        p.createProcess(3);
        p.createProcess(4);

        //teste da memória adicionando a fila
        mem.teste();
        //p.queueSize();
        //p.testeFila();
        //mem.teste();

        //teste dos base e bounds registers
        //p.testeRegisters();
        // p.testeRegisters();
        // p.testeRegisters();
        // p.testeRegisters();

        //posição 5 do Processo 1
        p.lerLinha(5);
        // posição 5 do processo 2
        p.lerLinha(5);
        // posição 5 do processo 3
        p.lerLinha(5);
        // posição 5 do processo 4
        p.lerLinha(5);

        // Verificar agora se ele retornar para o processo 1
        p.lerLinha(2);

    }
}