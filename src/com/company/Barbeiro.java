package com.company;


import static java.lang.Thread.sleep;

public class Barbeiro implements Runnable {

    FilaEspera filaEspera;
    public static long id;
    public static int status;

    Barbeiro(FilaEspera filaEspera){
        this.filaEspera = filaEspera;
    }

    public void getCliente(long idCliente) throws InterruptedException {
        System.out.println("[BAR]>>O barbeiro " + this.id + " esta atendendo o cliente " + idCliente);
        sleep(500);

    }


    @Override
    public synchronized void run() {
        this.id = Thread.currentThread().getId();
        System.out.println("[SYS]>Barbeiro " + this.id + " foi criado.");
        while(this.filaEspera.clientesAgendados() > 0 ){
            System.out.println("[SYS]>Barbeiro " + this.id + " acordou...");
            //Aqui eu tenho 3 situacoes, enquanto tem cliente na fila, ele fica pegando o proximo
            if(this.filaEspera.getFilaSize() != 0){
                Cliente cliente = this.filaEspera.getNextCliente();
                try {
                    this.filaEspera.alteraDisponiveis(1);
                    getCliente(cliente.id);
                    cliente.finalizado();
                    this.filaEspera.alteraDisponiveis(0);
                    this.filaEspera.decrementaClientes();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                //Se nao tem cliente na fila, ele pode dormir se ainda existir clientes a entrar na loja
                System.out.println("[SYS]>Barbeiro " + this.id + " indo dormir....");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("[BAR]>Barbeiro " + this.id + " batendo o ponto e indo pra casa.");
    }

}
