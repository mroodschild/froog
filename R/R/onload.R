.onLoad <- function(libname, pkgname){
  library('rJava')
  .jinit()
  .jpackage(pkgname, lib.loc=libname)
  .jaddLibrary('froog', 'inst/java/froog-0.4.jar')
  .jaddClassPath('inst/java/froog-0.4,jar')
}
