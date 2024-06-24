package visualizer.painter.instances;

public class Coordinates {
    private double x;
    private double y;

    public void setValue(double[] newValue) {
        assert newValue.length == 2;
        x = newValue[0];
        y = newValue[1];
    }

    public void setValue(int[] newValue) {
        assert newValue.length == 2;
        x = newValue[0];
        y = newValue[1];
    }

    public void setValue(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setValue(Coordinates newValue) {
        setValue(newValue.x, newValue.y);
    }

    public int[] getIntValue() {
        var res = new int[2];
        res[0] = (int) x;
        res[1] = (int) y;
        return res;
    }

    public void add(int[] other) {
        assert other.length > 1;
        x += other[0];
        y += other[1];
    }

    public void add(double[] other) {
        assert other.length > 1;
        x += other[0];
        y += other[1];
    }

    public void add(Coordinates other) {
        x += other.x;
        y += other.y;
    }

    public void subtract(int[] other) {
        assert other.length > 1;
        x -= other[0];
        y -= other[1];
    }

    public void subtract(double[] other) {
        assert other.length > 1;
        x -= other[0];
        y -= other[1];
    }

    public void subtract(Coordinates other) {
        x -= other.x;
        y -= other.y;
    }

    public void mul(double multiplier) {
        x *= multiplier;
        y *= multiplier;
    }

    public Coordinates copyAdd(int[] other) {
        assert other.length > 1;
        var res = new Coordinates();
        res.x = x + other[0];
        res.y = y + other[1];
        return res;
    }

    public Coordinates copyAdd(double[] other) {
        assert other.length > 1;
        var res = new Coordinates();
        res.x = x + other[0];
        res.y = y + other[1];
        return res;
    }

    public Coordinates copyAdd(Coordinates other) {
        var res = new Coordinates();
        res.x = x + other.x;
        res.y = y + other.y;
        return res;
    }

    public Coordinates copySubtract(int[] other) {
        assert other.length > 1;
        var res = new Coordinates();
        res.x = x - other[0];
        res.y = y - other[1];
        return res;
    }

    public Coordinates copySubtract(double[] other) {
        assert other.length > 1;
        var res = new Coordinates();
        res.x = x - other[0];
        res.y = y - other[1];
        return res;
    }

    public Coordinates copySubtract(Coordinates other) {
        var res = new Coordinates();
        res.x = x - other.x;
        res.y = y - other.y;
        return res;
    }

    public Coordinates copyMul(double multiplier) {
        var res = new Coordinates();
        res.x = x * multiplier;
        res.y = y * multiplier;
        return res;
    }

    public Coordinates getCopy() {
        var copy = new Coordinates();
        copy.setValue(x, y);
        return copy;
    }

    public Coordinates getAbs() {
        var res = new Coordinates();
        res.setValue(Math.abs(x), Math.abs(y));
        return res;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSquaredLength() {
        return x * x + y * y;
    }
}
