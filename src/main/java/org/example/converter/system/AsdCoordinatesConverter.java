package org.example.converter.system;

public class AsdCoordinatesConverter extends Converter{

    private static final double DEG2RAD = Math.PI / 180.0;
    private static final double RAD2DEG = 180.0 / Math.PI;
    private static final double EPSILON = 0.00001; // e-05 e-08 e-10
    private static final double R_MAJOR = 6378100.036; // 6378245.0; // Радиус земли
    private static final double EKV1 = 0.00669342;
    private static final double EKV2 = 0.00673853;
    private static final double _dx = -5700190.96788101;
    private static final double _dy = -99706.1503331292;
//    private static final double _dz = 0;
    private static final double _meridian = 117;
//    private static final double _Xo68 = 0;
//    private static final double _Yo68 = 0;
//    private static final double _CosB = 0;
//    private static final double _SinB = 0;
//    private static final double _Xo42 = 0;
//    private static final double _Yo42 = 0;
//    private static final double _ScaleX = 1;
//    private static final double _ScaleY = 1;
//    private static final boolean _Convert = false;

    private double A0(double e) {
        return 1.0+3.0*e /4.0+45.0*e *e /64.0;
    }

    private double B0(double e) {
        return e * 3.0 / 4.0 + e * 15.0 * e / 16.0;
    }

    private double C0(double e) {
        return e * 15.0 * e / 64.0;
    }

    private double M_ot_B(double B) {
        return R_MAJOR *(1.0-EKV1)*
            (A0(EKV1)*B -B0(EKV1)*
            Math.sin(2.0*B)/2.0+C0(EKV1)*Math.sin(4.0*B)/4.0)+0.197;
    }
    private double B_ot_M(double M) {
        double tekB = Math.PI / 4.0;
        double oldB = 0.0;
        double tekM = M_ot_B(tekB);
        double delta = tekM - M;
        double zn = 0.0;
        double newB = 0.0;

        if (delta < 0.0) {
            zn = -1.0;
            delta = -delta;
        }
        else {
            zn = 1.0;
        }

        while (delta > EPSILON) { // был цикл for index < 500 - 50000 - 100000
            if (zn > 0.0) {
                if (oldB < tekB)
                    newB = (tekB + oldB) / 2.0;
                else
                    newB = tekB - (oldB - tekB) / 2.0;
            } else {
                if (oldB < tekB)
                    newB = tekB + (tekB - oldB) / 2.0;
                else
                    newB = (tekB + oldB) / 2.0;
            }

            oldB = tekB;
            tekB = newB;
            tekM = M_ot_B(tekB);
            delta = tekM - M;

            if (delta < 0.0) {
                zn = -1.0;
                delta = -delta;
            } else
                zn = 1.0;
        }
        return tekB;
    }

    @Override
    public Converter.Point convertToBL(double x, double y) {

//        if (this._Convert)
//            pointXY = this.Convert68_to_42(pointXY);

        // Общие параметры для X,Y
        double b1 = B_ot_M(y - _dx);
        double s = Math.sin(b1);
        double c = Math.cos(b1);
        double t = s / c;
        double nukv = EKV2 * c * c;
        double N1 = R_MAJOR / Math.sqrt(1.0 - EKV1 * s * s);
        double aa = 1.0 - EKV1 * s * s;
        double M1 = R_MAJOR * (1.0 - EKV1) / Math.sqrt(aa * aa * aa);
        double yn2 = ((x - _dy) / N1) * ((x - _dy) / N1);

        // Параметры для X (lat (xy_to_B))
        double a1 = ((x - _dy) * t * (x - _dy)) / (M1 * 2.0 * N1);
        double a2 = 1.0 - yn2 * (5.0 + 3.0 * t * t + nukv - 9.0 * nukv * t * t) / 12.0;
        double a3 = yn2 * yn2 * (61.0 + 90.0 * t * t + 45.0 * t * t * t * t) / 360.0;
        double resultLat = b1 - (a1 * (a2 + a3));

        // Параметры для Y (lon (xy_to_L))
        a1 = (x - _dy) / (N1 * c);
        a2 = 1.0 - yn2 * (1.0 + 2.0 * t * t + nukv) / 6.0;
        a3 = yn2 * yn2 * (5.0 + 28.0 * t * t + 24.0 * t * t * t * t + 6.0 * nukv + 8.0 * nukv * t * t) / 120.0;
        double resultLon = a1 * (a2 + a3);

        return new Converter.Point(
                resultLat * RAD2DEG,
                (resultLon + _meridian * DEG2RAD) * RAD2DEG);
    }
}
