##levantamos los datos
x <- read.csv(url("https://raw.githubusercontent.com/mroodschild/froog-examples/master/src/main/resources/mnist/mnist_train_in_50000.csv
"), sep = ",", header = FALSE)
y <- read.csv(url("https://raw.githubusercontent.com/mroodschild/froog-examples/master/src/main/resources/mnist/mnist_train_out_50000.csv
"), sep = ",", header = FALSE)

start.time <- Sys.time()
snnsModel <- mlp(x = x, y = y, size = 500, maxit = 5, learnFunc = "SCG", hiddenActFunc = "Act_TanH")
end.time <- Sys.time()
time.taken <- end.time - start.time
time.taken

##generamos el modelo
model <- sequencial_model()
addLayer(net = model, input = 784, neurons = 300, activation = "tansig")
addLayer(net = model, input = 300, neurons = 10, activation = "softmax")

##configuramos el algoritmo de entrenamiento
start.time <- Sys.time()
scg(net = model,input = t(x), output = t(y), epochs = 2, loss_function = "crossEntropy", accuracy = FALSE)
end.time <- Sys.time()
time.taken <- end.time - start.time
time.taken
