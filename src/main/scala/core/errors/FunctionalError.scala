package core.errors

sealed trait FunctionalError                 extends RuntimeError
final case class WrongInput(message: String) extends FunctionalError
