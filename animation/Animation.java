
public class Animation {

    private double value;
    private double duration;
    private double last;

    private long start;

    public boolean animate(double toValue, double speed) {
        double part = (System.currentTimeMillis() - start) / duration;

        boolean alive = part < 1;

        if (alive) {
            value = value + (last - value) * part;

        } else {
            start = 0;
            value = last;
        }

        this.duration = speed * 1000;
        this.start = System.currentTimeMillis();
        this.last = toValue;

        return alive;
    }

    public double getValue() {
        return value;
    }

}