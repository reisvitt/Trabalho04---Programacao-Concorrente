package models;

import javafx.util.Duration;
import java.lang.Thread;
import controller.Controller;

/*********************************************************
 * Classe:  Produtor
 * Funcao: Classe responsável por produzir todos itens
 *         que serao inserido no buffer
 ********************************************************/
public class Produtor extends Thread{
  private int m1 = 1000000; // valor maximo a ser analisado e exibido no grafico
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
    int qtdPrimo = 0;
    for(int i = 1; i < m1; i++){ // maximo valor a ser analisado
      if(primo(i)){
        qtdPrimo++; // se o valor for primo, incrementa a variavel de contagem dos primos
      }// fim if

      if(i % 1000 ==  0){ // insere na fila ao analisar a quantidade de primos a cada 500 naturais
        try{
          sleep(veloz);
        }catch(InterruptedException e){
          e.printStackTrace();
        }

        this.controlador.produzir(qtdPrimo); // insere na fila
        qtdPrimo = 0; // zera o contador apos inserir na fila
      }
    }// fim for
  }// fim método run

  /*******************************************************************
   * Metodo: primo
   * Funcao: verifica se um numero eh primo
   * Parametros: long
   * Retorno: boolean
   *******************************************************************/
  private Boolean primo(long num){ 
    int aux = 0;
    int raiz = (int) Math.sqrt(num);
    
    for(int i = 1; i < num; i++){
      if(num % i == 0){
        aux++;
        if(aux > 1){
          return false;
        }
      }else if(i > raiz + 1){
        break;
      }

    }
    return true;
  }

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
   * Funcao: seta a velocidade do produtor alterando o valor do Sleep
   * Parametros: int
   * Retorno: void
   *******************************************************************/
  public void setVeloz(int veloz){
    this.veloz = veloz;
  }
}
