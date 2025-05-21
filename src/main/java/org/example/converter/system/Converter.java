package org.example.converter.system;

public abstract class Converter {

    public abstract Point convertToBL(double x, double y);

    public static class Point {
        private double longitude;
        private double latitude;


        public Point(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
