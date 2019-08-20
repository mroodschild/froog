/*
 * Copyright 2019 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.layer;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class TestBN {

    public static void main(String[] args) {
        SimpleMatrix A = SimpleMatrix.random_DDRM(3, 4, -1, 1, new Random(1));
        A.print();
        
        Batchnorm bn = new Batchnorm(3, 4);
        System.out.println("Forwardpass");
        bn.forwardpass(A);
        System.out.println("Backwardpass");
        bn.backwardpass(A);
    }

}
