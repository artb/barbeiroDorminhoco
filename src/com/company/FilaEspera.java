package com.company;

import com.sun.deploy.util.SessionState;

import java.util.ArrayList;

public class FilaEspera {

    public static int qntd_clientes;
    public static ArrayList<Cliente> filaClientes;
    public static ArrayList<Barbeiro> funcionarios;
    public static int capacidade;
    public static int disponiveis;


    public void clientesDia(int clientes){
        this.qntd_clientes = clientes;
    }

    public void criarFila(int capacidade){
        this.filaClientes = new ArrayList<>();
        this.funcionarios = new ArrayList<>();
        this.capacidade = capacidade;
    }

    public void adicionarBarbeiro(Barbeiro barbeiro){
        this.funcionarios.add(barbeiro);
        this.disponiveis = this.funcionarios.size();
    }

    public int getFilaSize(){
        return this.filaClientes.size();
    }

    public synchronized Cliente getNextCliente(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cliente cliente = this.filaClientes.get(0);
        this.filaClientes.remove(0);
        this.qntd_clientes--;
        notifyAll();
        return cliente;
    }

    public int clientesAgendados(){
        return this.qntd_clientes;
    }

    public void chegouCliente(){
        for(Barbeiro barbeiro: funcionarios){
            synchronized (barbeiro){
                barbeiro.notifyAll();
            }
        }

    }

    public void alteraDisponiveis(int valor){
        if(valor == 0){
            this.disponiveis += 1;
        }else{
            this.disponiveis -= 1;
        }
    }

    public void decrementaClientes(){
        this.qntd_clientes -=1;
    }

    public synchronized void clienteEntrou(Cliente cliente){
        if(this.filaClientes.size() < this.capacidade) {
            Cliente.atendido();
            //Posso inserir um cliente na fila, mas vou garantir primeiro que nao vai ter conflito
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.filaClientes.add(cliente);
            System.out.println("[CLI]>>Cliente " + cliente.id + " entrou na Barbearia.");
            notifyAll();
            //Agora eu falo pra galera que chegou um cliente novo
            chegouCliente();
            System.out.println("[SYS]> Barbeiros disponiveis no momento: " + this.disponiveis);

            if(this.disponiveis == 0){
                System.out.println("[CLI]>>Cliente " + cliente.id + " esta esperando para ser atendido.");
            }
        } else{
            System.out.println("[CLI]>>Cliente " + cliente.id + " nao conseguiu sentar e foi embora.");
            cliente.foiPraCasa();
            this.decrementaClientes();

        }
    }

}
