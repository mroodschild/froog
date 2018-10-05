.onLoad <- function(libname, pkgname){
  library('rJava')
  .jinit()
  .jpackage(pkgname, lib.loc=libname)
}
