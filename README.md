# Froog
                    Author: Roodschild Matías / mroodschild@gmail.com
                            Jorge Gotay Sardiñas / jgotay57@gmail.com
                            Adrián Will / adrian.will.01@gmail.com
                            

## Introduction

This project was created with academic propuose for my PhD Tesis. Its design goals are; 1) to be accessible to both novices and experts, and 2) facilitate neural networks manipulations. Froog is free, written in 100% Java and has been released under M.I.T. license.

Currently Froog supports:

* Backpropagation Algorithm
* Stochastic Gradient Descent
* Momentum
* Weight Normalization
* Loss Functions: RMSE, MSE, CrossEntropy
* Transfer Functions: Logsig, Tansig, Softmax, Purelim
* Transfer Functions (experimental): Softplus, ReLU
* Confusion Matrix


## Documentation

Not yet available

## Maven Central

Froog is in Maven jitpack.io and can easily be added to Maven, and similar project managers.

```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories> 
    <dependencies>
        <dependency>
            <groupId>com.github.mroodschild</groupId>
            <artifactId>froog</artifactId>
            <version>v0.2</version>
        </dependency>
        <dependency>
            <groupId>org.ejml</groupId>
            <artifactId>all</artifactId>
            <version>0.30</version>
        </dependency>
    </dependencies>
```


## Dependencies
-----------------------------------------

The main Froog modules depends on the following libraries

- [ EJML 0.30         ]  ( http://code.google.com/p/efficient-java-matrix-library )
- [ Apache Commons-csv          ]  ( https://commons.apache.org/proper/commons-csv/ )

The following is required for unit tests

- [ JUnit   ]       ( http://junit.sourceforge.net/                           )

## License

Froog is released under the M.I.T. open source license, but some libraries used in Froog are under Apache v2.0 License.
