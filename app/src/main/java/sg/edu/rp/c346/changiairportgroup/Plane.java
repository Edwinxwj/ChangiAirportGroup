package sg.edu.rp.c346.changiairportgroup;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class Plane {
        private String licensePlate;
        private String timing;
        private String toWhere;
        private String flightNum;
        private String airline;

        public Plane(String timing, String licensePlate, String airline, String flightNum, String toWhere){
            this.licensePlate = licensePlate;
            this.airline = airline;
            this.timing = timing;
            this.flightNum = flightNum;
            this.toWhere = toWhere;

        }
        public String getLicensePlate(){
            return licensePlate;
        }

        public String getFlightNum(){
        return flightNum;
    }
        public String getTiming(){
        return timing;
    }
        public String getAirline(){
        return airline;
    }
        public String getToWhere(){
        return toWhere;
    }


    }

