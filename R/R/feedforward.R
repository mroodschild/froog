#' Retrieve a Java instance of SpellCorrector.
#'
#' Retrieve a Java instance of SpellCorrector, with the training file
#' specified. Language model is trained before the instance is returned.
#' The spell corrector is adapted from Peter Norvig's demonstration.
#'
#'
#' @return a Java instance of SpellCorrector
#' @export
sequencial_model<-function() {
  .jaddLibrary('froog', 'inst/java/froog-0.4.jar')
  .jaddClassPath('inst/java/froog-0.4,jar')
  net <- .jnew('org/gitia/froog/r/rFeedforward')
  return(net)
}

#' This function adds a new layer to the neural network 'net'
#' \code{addLayer} adds a new layer to the neural network
#'
#'  @param net is the model to add a new layer.
#'  @param input number of inputs received by the neural network.
#'  @param neurons number of neurons in this layer.
#'  @param activation type of activation function, it can be "tansig", "logsig", "relu", "purelim", "softmax", "softplus".
addLayer<-function(net, input, neurons, activation){
  .jcall(net, 'V','addLayer', as.integer(input), as.integer(neurons), activation)
}

# Cambiar nombres y poner predict en vez de out, y a matrix ponerle input
#' @param net
#' @param matrix
out<-function(net, matrix){
  a <- .jcall(net, '[D','out', as.double(as.matrix(matrix)), as.integer(nrow(matrix)), as.integer(ncol(matrix)))
  nrowOutput <- .jcall(net,'I','getOutCount')
  m <- matrix(data = a, nrow = nrowOutput, ncol = as.integer(ncol(matrix)), byrow = TRUE)
  return(m)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param loss_function
#' @param acceleration
#' @param acc_param
#' @param accuracy
bp<-function(net, input, output, epochs = 100, loss_function ="rmse", acceleration = "", acc_param = 0.9, accuracy = FALSE){
  .jcall(net, 'V','bp',
         as.double(as.matrix(input)), as.integer(nrow(input)), as.integer(ncol(input)),
                             as.double(as.matrix(output)), as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         loss_function,
         acceleration,
         acc_param,
         as.logical(accuracy))
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param batch_size
#' @param loss_function
#' @param acceleration
#' @param acc_param
#' @param accuracy
sgd<-function(net, input, output, epochs = 100, batch_size = 10, loss_function ="rmse", acceleration = "", acc_param = 0.9, accuracy = FALSE){
  .jcall(net, 'V','sgd',
         as.double(as.matrix(input)), as.integer(nrow(input)), as.integer(ncol(input)),
         as.double(as.matrix(output)), as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         as.integer(batch_size),
         loss_function,
         acceleration,
         acc_param,
         accuracy)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param beta_rule
cg<-function(net, input, output, epochs = 100, beta_rule = "fletcher_reeves", loss_function ="rmse", accuracy = FALSE){
  .jcall(net, 'V','cg',
         as.double(as.matrix(input)), as.integer(nrow(input)), as.integer(ncol(input)),
         as.double(as.matrix(output)), as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         beta_rule,
         loss_function,
         accuracy)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
scg<-function(net, input, output, epochs = 100, loss_function ="rmse", accuracy = FALSE){
  .jcall(net, 'V','scg',
         as.double(as.matrix(input)),  as.integer(nrow(input)),  as.integer(ncol(input)),
         as.double(as.matrix(output)), as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         loss_function,
         accuracy)
}

setTestData<-function(net, input, output){
  .jcall(net, 'V', 'setTestData',
         as.double(as.matrix(input)), as.integer(nrow(input)), as.integer(ncol(input)),
         as.double(as.matrix(output)), as.integer(nrow(output)), as.integer(ncol(output))
         )
}

oneHot<-function(tags){
  net2<-sequencial_model()
  a <- .jcall(net2, '[D', 'oneHot', as.character(tags))
  classes <- .jcall(net2,'I','getNumClasses')
  m <- matrix(data = a, nrow = length(as.character(tags)), ncol = classes, byrow = TRUE)
  return(m)
}
