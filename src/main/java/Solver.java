
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.linear.*;


public class Solver {

    private final int numOfIntegrationPoints;
    IterativeLegendreGaussIntegrator solver;
    private static final double ACCURACY = Math.pow(0.1,4);
    private final double denominator;
    private static final double domain=2;
    private final double n;
    RealVector result;


    public Solver(int numOfIntegrationPoints) {
        this.numOfIntegrationPoints = numOfIntegrationPoints;
        this.solver = new IterativeLegendreGaussIntegrator(numOfIntegrationPoints, ACCURACY, ACCURACY);
        this.denominator = Math.abs(domain / numOfIntegrationPoints);
        this.n = numOfIntegrationPoints;
        this.solve(numOfIntegrationPoints);

    }


    public RealVector getResult() {
        return result;
    }


    private static double K(double x) {
        return (0<= x && x<=1) ? 1.0 : 2.0;
    }


    private double e(double x, int i){
        //y=ax+b
        if (x > (2*(i-1)/n) && x <= (2*i/n)){
            return (n/2*x) - i + 1;
        }
        else if (x <(2*(i+1)/n) && x > (2*i/n)){
            return -(n/2*x) + i + 1;
        }
        return 0;
    }

    //returns a coefficient depending of x belongs to an interval
    //where e(i) is increasing or deacreasing
    private double eDerivative(double x, int i){

        if (x > (2*(i-1)/n) && x <= (2*i/n)){
            return n/2;
        }
        else if (x > (2*i/n) && x < (2*(i+1)/n)){
            return -n/2;
        }
        return 0;
    }


    private void solve(int size) {

        RealMatrix matrix = new Array2DRowRealMatrix(size, size);

        for(int i = 0; i < size; i++) {
            for (int j =0; j<size;j++) {
                matrix.setEntry(i,j,0);
            }
        }

        for (int i = 0; i< size; i++) {
            for (int j = 0; j<=i+1;j++) {

                if(j<size && Math.abs(i-j)<=1) {
                    matrix.setEntry(i ,j,
                            B_u_v(i, j)

                    );
                }

            }
        }


        RealVector lVector = new ArrayRealVector(size, 0);

        for(int i = 0; i< size;i++) {
            lVector.setEntry(i, L_v(i));
        }
        System.out.println("B(ei,ej) matrix=" + lVector);

        for(int i = 0; i< size; i++) {
            System.out.println(matrix.getRowMatrix(i));
        }
        System.out.println("L(ei) vector=" + lVector);

        try {
            DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
            this.result = solver.solve(lVector);
            System.out.println("result=" + result);

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


    private double[] e_domain(int i, double xivalue) {

        //returns a domain of e function where e is not 0;

        if (i>0 && i <numOfIntegrationPoints) {
            return new double[]{ xivalue-denominator, xivalue+denominator};
        }

        else if (i==0) {
            return new double[]{0, denominator};
        }

        else {
            return new double[]{2-denominator, 2};
        }
    }

    private double B_u_v( int i, int j) {

        double[] eiDomain = e_domain(i,i*denominator);
        double[] ejDomain = e_domain(j,j*denominator);

        double integrateFrom = Math.round(Math.max(eiDomain[0], ejDomain[0])*100.0)/100.0;
        double integrateTo =   Math.round(Math.min(eiDomain[1], ejDomain[1])*100.0)/100.0;


        double solution = solver.integrate(
                Integer.MAX_VALUE,
                x ->  K(x) * eDerivative(x,i) * eDerivative(x,j),
                integrateFrom,
                integrateTo
        );


        //e(0,n) = 0 for n>=2;

        double vtimesu = 0;

        if (i < 2 && j <2) {
             vtimesu = -K(0)*e(0,i) * e(0,j);
        }

        return solution+vtimesu ;

    }


    private double L_v(int i ) {
        return -K(0)*20*e(0,i);
    }

}
