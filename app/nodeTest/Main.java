package nodeTest;

/**
 * Created by hc2twv on 22/2/17.
 */
public class Main {
    public static void main(String args[]) {
        int num_nodes = 7; // Set up the configuration about how much nodes you have.
        for(int i=1; i<=num_nodes; i++){
            RunNode n1 = new RunNode(i, "982347238942"+i);
            n1.start();
        }
    }
}

class RunNode implements Runnable {
    private Thread t;
    private int id;
    private String MAC;

    RunNode(int _id, String _MAC) {
        id = _id;
        MAC = _MAC;
        System.out.println("Creating Node " +  id );
    }

    public void run() {
        new NodeGUI(this.id, this.MAC);
    }

    public void start () {
        System.out.println("Starting " +  id );
        if (t == null) {
            t = new Thread (this, "Node: "+id);
            t.start ();
        }
    }
}
