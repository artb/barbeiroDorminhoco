package com.company;

public class Cliente implements Runnable{

    public static long id;
    FilaEspera filaEspera;
    public static boolean status = false;
    public static boolean atendido = false;

    public Cliente(FilaEspera filaEspera){
        this.filaEspera = filaEspera;

    }

    public void foiPraCasa(){
        Thread.currentThread().interrupt();
    }

    public static void atendido(){
        atendido = false;
    }

    public synchronized void finalizado(){
        System.out.println("[CLI]>Cliente " + id + " cortou o cabelo e esta indo pra casa...");
        notifyAll();
        Thread.currentThread().interrupt();

    }

    @Override
    public synchronized void run() {
        this.id = Thread.currentThread().getId();
        System.out.println("[SYS]>Cliente " + id + " foi criado.");
        filaEspera.clienteEntrou(this);
        while(!this.atendido){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
