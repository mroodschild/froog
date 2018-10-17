/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.froog.optimizer.testfunction;

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
