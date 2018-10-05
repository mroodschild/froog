#' Retrieve a Java instance of SpellCorrector.
#'
#' Retrieve a Java instance of SpellCorrector, with the training file
#' specified. Language model is trained before the instance is returned.
#' The spell corrector is adapted from Peter Norvig's demonstration.
#'
#'
#' @return a Java instance of SpellCorrector
#' @export
getFeedforward<-function() {
  .jaddLibrary('froog', 'inst/java/froog-0.4.jar')
  .jaddClassPath('inst/java/froog-0.4,jar')
  net <- .jnew('org/gitia/froog/r/rFeedforward')
  return(net)
}

#'  @param net
#'  @param input
#'  @param neurons
#'  @param activation
addLayer<-function(net, input, neurons, activation){
  .jcall(net, 'V','addLayer', as.integer(input), as.integer(neurons), activation)
}

out<-function(net, matrix){
  a <- .jcall(net, '[D','out', matrix, as.integer(nrow(matrix)), as.integer(ncol(matrix)))
  return(a)
}
