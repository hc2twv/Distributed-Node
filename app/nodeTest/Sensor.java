package nodeTest;

import java.util.Random;

/**
 * Created by hc2twv on 22/2/17.
 */
public class Sensor {
    private int id;
    private Double umbral_inf;
    private Double umbral_sup;
    private String type;
    Random r = new Random();

    public Sensor(int _id, double _umbral_inf, double _umbral_sup, String _type){
        this.id = _id;
        this.umbral_inf = _umbral_inf;
        this.umbral_sup = _umbral_sup;
        this.type = _type;
    }

    public Double getValue(){
        return r.nextDouble()*(this.umbral_sup - this.umbral_inf) + this.umbral_inf;
    }

    public void setUmbral_inf(double umbral_inf) {
        this.umbral_inf = umbral_inf;
    }

    public void setUmbral_sup(double umbral_sup) {
        this.umbral_sup = umbral_sup;
    }

    public int getId() {
        return id;
    }

    public Double getUmbral_inf() {
        return umbral_inf;
    }

    public Double getUmbral_sup() {
        return umbral_sup;
    }

    public String getType() {
        return type;
    }
}
