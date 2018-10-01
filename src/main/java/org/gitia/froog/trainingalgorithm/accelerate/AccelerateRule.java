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
package org.gitia.froog.trainingalgorithm.accelerate;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class AccelerateRule {

    public static Accelerate nonAccelerate() {
        return new NonAccelerate();
    }

    public static Accelerate momentum(double beta) {
        return new Momentum(beta);
    }
    
    public static Accelerate momentumRumelhart(double beta) {
        return new MomentumRumelhart(beta);
    }

    /**
     * 
     * @param beta1 recommended 0.9
     * @param beta2 recommended 0.999
     * @param epsilon recommended 1e-8
     * @param t recommended 2
     * @return 
     */
    public static Accelerate adam(double beta1, double beta2, double epsilon, double t) {
        return new Adam(beta1, beta2, epsilon, t);
    }  
    
}