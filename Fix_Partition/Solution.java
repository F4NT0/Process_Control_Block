import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * =================================================
 * SOLUÇÃO DA QUESTÃO 1 DA GII - PARTIÇÕES FIXAS
 * Autor: Gabriel Fanto Stundner
 * =================================================
 */

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

class ContextData{
    int r1,r2,r3,r4,r5,r6,r7,r8;
    int idProcess;
    public ContextData(int idProcess){
        this.idProcess = idProcess;
    }
    public int getRegister(String register){
        switch (register) {
            case "r1": return r1;case "r2": return r2;
            case "r3": return r3;case "r4": return r4;
            case "r5": return r5;case "r6": return r6;
            case "r7": return r7;case "r8": return r8;
        }
        return -1;
    }
    public void save(int r1,int r2,int r3, int r4, int r5, int r6, int r7, int r8){
        this.r1 = r1;this.r2 = r2;this.r3 = r3;this.r4 = r4;
        this.r5 = r5;this.r6 = r6;this.r7 = r7;this.r8 = r8;
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
    // Salvar o Contexto
    ContextData context = new ContextData(id);

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

    // Salvando o Contexto Atual
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

    // Leitura de uma Linha de cada Processo
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
            System.out.println("Posição " + value + " | Processo " + process.getId() + " : " + mem.getMemory().get(value) + "\n");
            process.saveContextData(value,-1, -1, -1, -1, -1, -1, -1);
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
    public void testeContextData(){
        Process aux = queue.poll();
        System.out.println("Processo: " + aux.getId());
        System.out.println("ContextData: " + aux.getRegister("r1") + "\n");
    }

}

public class Solution{
    public static void main(String[] args){
        // Inciando a Memória e o PCB
        Memory mem = new Memory();
        PCB p = new PCB(mem);

        // Criação de 4 Processos
        p.createProcess(1);
        p.createProcess(2);
        p.createProcess(3);
        p.createProcess(4);

        // Teste verificando a memória
        System.out.println("Memória Após iniciado quatro Processos");
        mem.teste();

        // Teste da memória adicionando a fila
        // p.queueSize();
        // p.testeFila();
        // mem.teste();

        // Teste dos base e bounds registers
        // p.testeRegisters();
        // p.testeRegisters();
        // p.testeRegisters();
        // p.testeRegisters();

        // Teste de leitura dos Processos
        System.out.println("\n Lendo Posicoes vindas do Programa(ex: posição 5 em cada Processo)");
        p.lerLinha(5); // posição 5 do Processo 1
        p.lerLinha(5); // posição 5 do processo 2
        p.lerLinha(5); // posição 5 do processo 3
        p.lerLinha(5); // posição 5 do processo 4

        // Teste de Verificação se está salvando o contexto
        // p.lerLinha(5);
        // p.lerLinha(2);
        // p.lerLinha(4);
        // p.lerLinha(6);
        System.out.println("\n Verificando se está salvando no ContextData as Posições");
        p.testeContextData();
        p.testeContextData();
        p.testeContextData();
        p.testeContextData();

    }
}