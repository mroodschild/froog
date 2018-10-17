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
package org.gitia.froog.optimizer.accelerate;

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
