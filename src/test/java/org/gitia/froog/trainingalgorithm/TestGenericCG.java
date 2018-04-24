/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.trainingalgorithm;

import java.util.Random;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class TestGenericCG {

    public TestGenericCG() {
    }

    public void run(SimpleMatrix x, int par) {
        double rho = 0.01, beta = 0.1, maxit = 1000, f;
        SimpleMatrix g;
        f = funcion(x);//hay que ver para mutiples salidas
        g = g(x);

        SimpleMatrix ppr = g.negative();
        SimpleMatrix gpr = g.copy();
        double pbeta = 0;

        for (int i = 1; i <= maxit; i++) {//backprop

            if (i > 1) {
                f = funcion(x);
                g = g(x);
                g.print();
                if (par == 1) {
                    // Fletcher - Reeves
                    pbeta = (g.transpose().mult(g)).get(0) / (gpr.transpose().mult(gpr)).get(0);
                } else {
                    // Polak - Ribière
                    pbeta = g.minus(gpr).transpose().mult(g).get(0) / gpr.transpose().mult(gpr).get(0);
                }
            }

            SimpleMatrix p = ppr.scale(pbeta).minus(g);
            double norm = NormOps_DDRM.normPInf(g.getDDRM());
            if (norm < 1e-6) {//     % Condición de mínimo
                break;
            }
            double alpha = 1;
            for (int k = 0; k < 10; k++) {
                SimpleMatrix xnew = x.plus(p.scale(alpha));
                double fxnew = funcion(xnew);
                if (fxnew <= f + alpha * rho * g.transpose().mult(p).get(0)) {
                    break;
                } else {
                    alpha = alpha * beta;
                }
            }

            x = x.plus(p.scale(alpha));
            ppr = p;
            gpr = g;
            System.out.println(i + "\t" + x.get(0) + "\t" + x.get(1) + "\t" + f + "\t" + alpha + "\t" + norm);
        
            System.out.println("g");
            g.printDimensions();
            System.out.println("ppr");
            ppr.printDimensions();
            System.out.println("gpr");
            gpr.printDimensions();
            System.out.println("p");
            p.printDimensions();
            System.exit(0);
        }
        
    }

    public double funcion(SimpleMatrix x) {
        double x1 = x.get(0);
        double x2 = x.get(1);
        return 100 * Math.pow(x2 - Math.pow(x1, 2), 2) + Math.pow(1 - x1, 2);
    }

    public SimpleMatrix g(SimpleMatrix x) {
        double x1 = x.get(0);
        double x2 = x.get(1);
        double g[] = new double[2];
        g[0] = -200 * (x2 - Math.pow(x1, 2)) * 2 * x1 - 2 * (1 - x1);
        g[1] = 200 * (x2 - Math.pow(x1, 2));
        return new SimpleMatrix(2, 1, true, g);
    }

    public static void main(String[] args) {
        TestGenericCG test = new TestGenericCG();
        Random r = new Random(1);
        SimpleMatrix x = SimpleMatrix.random_DDRM(2, 1, -1, 1, r);
        x.print();
        test.run(x, 2);
    }
}
