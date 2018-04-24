/*
 * The MIT License
 *
 * Copyright 2018 Matías Rodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.trainingalgorithm.testfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class TestRosenbrock implements TestFunction {

    double a = 1;
    double b = 100;

    /**
     * Rosenbrock's Function<br>
     * f(x, y) = (a - x)<sup>2</sup> + b (y - x<sup>2</sup>)<sup>2</sup>
     *
     * @param X [2 x 1]
     * @return output of function
     */
    @Override
    public double compute(SimpleMatrix X) {
        double x = X.get(0);
        double y = X.get(1);
        return Math.pow(a - x, 2) + b * Math.pow(y - Math.pow(x, 2), 2);
    }

    /**
     * gradient(f'x, f'y) =<br>
 gradient( -2 (a - x) - 4 b x (y - x<sup>2</sup>) ; 2 b (y - x<sup>2</sup>))
     * 
     * @param X [2 x 1]
     * @return gradient [2 x 1]
     */
    @Override
    public SimpleMatrix gradient(SimpleMatrix X) {
        double x = X.get(0);
        double y = X.get(1);
        double[] f = new double[2];
        f[0] = -2 * (a - x) - 4 * b * x * (y - Math.pow(x, 2));
        f[1] = 2 * b * (y - Math.pow(x, 2));
        return new SimpleMatrix(2, 1, true, f);
    }

}
