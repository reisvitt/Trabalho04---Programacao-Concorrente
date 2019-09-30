/**********************************************************************************************
  * Autor: Vitor de Almeida Reis
  * Matricula: 201710793
  * Inicio: 24/09/2019
  * Ultima alteracao: 29/09/2019 
  * Nome: Contador de primos
  * Funcao: Simulacao resolvendo problemas de concorrencia do Produtor/Consumidor (Semaphore)
  *********************************************************************************************/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import controller.Controller;

public class Principal extends Application {
  static Stage primaryStage; 

  @Override 
  public void start(Stage home) {
    this.primaryStage = home;
    try{
      Parent principalfxml = FXMLLoader.load(getClass().getResource("/views/telaPrincipal.fxml"));
      Scene scenaPrincipal = new Scene(principalfxml);
      primaryStage.setScene(scenaPrincipal);
      primaryStage.show();
    } catch(IOException e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    launch(args);
  }//fim método main
}// fim classe
