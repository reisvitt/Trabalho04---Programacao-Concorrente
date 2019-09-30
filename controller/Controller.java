package controller;

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import java.net.URL;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.lang.Thread;
import javafx.scene.chart.LineChart;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javafx.scene.control.Slider;
import models.Produtor;
import models.Consumidor;
import java.text.DecimalFormat;

/******************************************************
 * Classe:  Controller
 * Funcao: Classe responsável por controlar todas as
 *         acoes da view telaPrincipal.fxml
 * 
 *****************************************************/
public class Controller implements Initializable{

  @FXML
  private AnchorPane anchorPane, presentation;

  @FXML
  private ProgressBar progressBarIndicator;

  @FXML
  private Slider slider1, slider2;

  @FXML
  private Button btnPause;

  @FXML
  private Label velocidadeProdutor, velocidadeConsumidor;

  @FXML
  private LineChart<String, Number> lineChart;
  XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

  private int controle = 0;
  private boolean isPaused = false;

  private Produtor produtor;
  private Consumidor consumidor;

  private int TAMANHO_MAXIMO = 10;

  private Queue<Integer> buffer;
  private Semaphore mutex = new Semaphore(1);
  private Semaphore cheio = new Semaphore(TAMANHO_MAXIMO);
  private Semaphore vazio = new Semaphore(TAMANHO_MAXIMO);

  //Construtor
  public Controller(){
    //inicialar e instanciar as Threads Produtor e Consumidor
    produtor = new Produtor();
    consumidor = new Consumidor();

    buffer = new LinkedList<>();// inicializa o buffer

    try{
      this.vazio.acquire(TAMANHO_MAXIMO); // inializa o contador vazio com o valor maximo
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }

  /************************************************************
   * Metodo: initialize
   * Funcao: metodo pradrao no javafx
   * Parametros: URL, ResourceBudle
   * Retorno: void
   ***********************************************************/
  @Override
  public void initialize(URL url, ResourceBundle rb){
    produtor.setControlador(this);
    consumidor.setControlador(this);
    series.setName("Quantidade");

    produtor.setDaemon(true);
    consumidor.setDaemon(true);

    lineChart.getData().add(series);
  }

  /************************************************************
   * Metodo: velocidadeProdutor
   * Funcao: seta a velocidade do produtor de acordo com o slider
   * Parametros: void
   * Retorno: void
   ***********************************************************/
  @FXML
  public void velocidadeProdutor(){ // Define a velocidade do produtor de acordo com o slider
    int aux = (int) slider1.getValue();
    double num1 = (2000 - (aux * 200));

    produtor.setVeloz((int) num1);
    
    DecimalFormat df = new DecimalFormat("0.0");
    
    double num = 1000 / num1;
  
    velocidadeProdutor.setText(df.format(num) + " por segundo"); // exibe a velocidade na tela

  }


  /************************************************************
   * Metodo: velocidadeConsumidor
   * Funcao: seta a velocidade do produtor de acordo com o slider
   * Parametros: void
   * Retorno: void
   ***********************************************************/
  @FXML
  public void velocidadeConsumidor(){ // Define a velocidade do consumidor de acordo com o slider
    int aux = (int) slider2.getValue();
    double num1 = (2000 - (aux * 200));
    consumidor.setVeloz((int) num1);

    double num = 1000 / num1;
    DecimalFormat df = new DecimalFormat("0.0");

    velocidadeConsumidor.setText(df.format(num) + " por segundo"); // exibe a velocidade na tela

  }

  /************************************************************
   * Metodo: produzir
   * Funcao: coloca os produtos no buffer
   * Parametros: int (qtdPrimo) -> quantidade de numeros primos
                 a cada 1k
   * Retorno: void
   ***********************************************************/
  public void produzir(int qtdPrimo){ // recebe dado

    try{
      
      //verificar tamanho maximo e se estiver cheio, parar de produzir
      this.cheio.acquire();
      
      this.mutex.acquire(); // entrar na Rc
      buffer.add(qtdPrimo); // add no buffer
      progressBarIndicator.setProgress(progressBarIndicator.getProgress() + 0.1); // aumenta o progressBar que representa o buffer
      // 0.1 por que o buffer possui no maximo 10 elementos, logo cada elemento representa 10% = 0.1

      this.mutex.release(); // deixa RC
      this.vazio.release(); //decrementa a quantidade de vazio
      
    }catch(Exception e){
      e.printStackTrace();
    }

  }

  /************************************************************
   * Metodo: adicinarAoGrafico
   * Funcao: Consumir o produto adicionando ao grafico
   * Parametros: int (quantidade) -> quantidade de numeros primos
                 a cada 1k
   * Retorno: void
   ***********************************************************/
  public void adicinarAoGrafico(int quantidade){
    controle++;
    Platform.runLater(() -> {
        series.getData().add(new XYChart.Data<String, Number>((controle) + "k", quantidade));
    });
  }
  
  /*********************************************************************
   * Metodo: consumir
   * Funcao:  Retirar o produto do buffer e chamar metodo para consumir
   * Parametros: void
   * Retorno: void
   **********************************************************************/
  public void consumir(){
    Integer qtdPrimo = 0;
    try{
      
      this.vazio.acquire(); // incrementa a quantidade de vazio

      mutex.acquire(); // entra RC

      qtdPrimo = buffer.remove(); // retirar item da RC
      progressBarIndicator.setProgress(progressBarIndicator.getProgress() - 0.1); // reduz o progressBar que representa o buffer

      this.mutex.release(); // sai da RC

      this.cheio.release(); //decrementa a quantidade de Cheio
      
    }catch(Exception e){
      e.printStackTrace();
    }

    adicinarAoGrafico(qtdPrimo); // altera o grafico /  consome dado
  }

  /*********************************************************************
   * Metodo: apresentacao
   * Funcao:  Exibe a tela de apresentacao que explicacao do programa
   * Parametros: void
   * Retorno: void
   **********************************************************************/
  @FXML
  private void apresentacao(){
    presentation.setVisible(false);
    consumidor.start();
    produtor.start();
  }


  /*****************************************************************************
   * Metodo: pause
   * Funcao: Pausa o programa, pausando as Thredas que Produz e a que Consome
   * Parametros: void
   * Retorno: void
   ******************************************************************************/
  @FXML
  private void pause(){
    if(isPaused){
      btnPause.setText("Pause");
      produtor.resume();
      consumidor.resume();
      isPaused = false;
    }else{
      btnPause.setText("Continuar");
      produtor.suspend();
      consumidor.suspend();
      isPaused = true;
    }
  }
}
