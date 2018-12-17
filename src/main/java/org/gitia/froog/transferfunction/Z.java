/*
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.transferfunction;

import java.util.stream.IntStream;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Z {
    
    public static SimpleMatrix output(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        SimpleMatrix aux = W.mult(a);
        int size = aux.numCols();
        IntStream.range(0, size).parallel()
                .forEach(i -> aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData()));
        return aux;
    }
    
}