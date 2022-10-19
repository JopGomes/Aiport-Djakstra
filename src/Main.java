import Menu.*;

import java.util.ArrayList;

public class
Main {
    public static void main(String[] args) {
        //Variavel que ira armazenar um objeto Menu
        Menu Section = new Menu();
        //Chama o metodo start da classe menu para inicializar uma sessao
        //Com Thread
        //Section.startThread();
        //Sem thread
        Section.start();
    }
}
