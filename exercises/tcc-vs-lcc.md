# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

le TCC et la LCC possèderont la même valeur si toutes les méthodes utilises tous les mêmes attributs (voir class Point ci-dessous). La LCC ne pourra jamais être plus basse que la TCC puisque la LLC est une extension de la TCC et qu'elle ne peut qu'augmenter le nombre de liaisons.

class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double[] getXY() {
        return {this.x,this.y};
    }

    public double dot(Point p) {
        return x*p.x + y*p.y;
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }
