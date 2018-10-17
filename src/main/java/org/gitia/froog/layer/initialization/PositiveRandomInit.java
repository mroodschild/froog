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
package org.gitia.froog.layer.initialization;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class PositiveRandomInit implements WeightInit {

    Random random = new Random();

    /**
     * <b>Positive random numbers</b>: we still want the weights to be positive,
     * but between zero and one.
     * <br><br>
     *
     * @param matrix
     */
    @Override
    public void init(SimpleMatrix matrix) {
        for (int i = 0; i < matrix.getNumElements(); i++) {
            matrix.set(i, random.nextDouble());
        }
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }
    
    @Override
    public String toString() {
        return "positiverandom";
    }
}
