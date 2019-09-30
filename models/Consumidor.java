package models;

import javafx.util.Duration;
import java.lang.Thread;
import controller.Controller;

/*********************************************************
 * Classe:  Produtor
 * Funcao: Classe responsável por consumir todos itens
 *         que estao no buffer
 ********************************************************/
public class Consumidor extends Thread{

  private int veloz = 2000;
  private Controller controlador;

  /*******************************************************************
   * Metodo: run
   * Funcao: Escopo da Thread
   * Parametros: void
   * Retorno: void
   *******************************************************************/
  @Override
  public void run(){
    while(true){
      this.controlador.consumir();

      try{
        sleep(veloz);
      }catch(InterruptedException e){
        e.printStackTrace();
      }

      
    }//fim while
  }// fim método run

  /*******************************************************************
   * Metodo: setControlador
   * Funcao: seta o Controller
   * Parametros: Controller
   * Retorno: void
   *******************************************************************/
  public void setControlador(Controller controlador){
    this.controlador = controlador;
  }

  /*******************************************************************
   * Metodo: vetVeloz
   * Funcao: seta a velocidade do consumidor alterando o valor do Sleep
   * Parametros: int
   * Retorno: void
   *******************************************************************/
  public void setVeloz(int veloz){
    this.veloz = veloz;
  }
}
