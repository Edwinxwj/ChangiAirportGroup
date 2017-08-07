package sg.edu.rp.c346.changiairportgroup;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class Plane {
    private String licensePlate;
    private Long time;
    private String destination;
    private String flightNo;
    private String direction;
    private String airline;

    public Plane() {

    }

    public Plane(String licensePlate, Long time, String destination, String flightNo, String direction, String airline) {
        this.licensePlate = licensePlate;
        this.time = time;
        this.destination = destination;
        this.flightNo = flightNo;
        this.direction = direction;
        this.airline = airline;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
