import monitor.AutoManager;
import view.MainView;

public class Main {

    public static void main(String[] args) {

        MainView view = new MainView();

        new Thread(() -> {
            AutoManager manager = new AutoManager(view);
            manager.iniciar();
        }).start();
    }
}