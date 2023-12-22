public class PrivateCar {
    private int seats = 4;
    private int wheels = 4;
    private TypeOfCar typeOfCar;

    public PrivateCar(int seats, int wheels, TypeOfCar typeOfCar){
        this.seats = seats;
        this.wheels = wheels;
        this.typeOfCar = typeOfCar;
    }

    public int getSeats() {
        return seats;
    }

    public int cost() {
        int cost = 1;
        switch (typeOfCar) {
            case FAMILY: cost = 2;
                break;
            case OFFROAD: cost = 3;
                break;
            case SUPERCAR: cost = 4; 
                break;
            default:
                break;
        }
        return cost*seats;
    }

    public float stability() {
        float stabilityFactor = 1.25f;
        return stabilityFactor * seats;
    }
    
}