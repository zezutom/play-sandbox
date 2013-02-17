package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import views.html.defaultpages.unauthorized

object Application extends Controller {

  def LoggingAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action { request =>
      Logger.info("Calling action")
      f(request)
    }
  }
  
  def index = Action {
    Ok(views.html.index("Hello world!"))
  }

  def indexPlain = Action {
    SimpleResult(
      header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/plain")),
      body = Enumerator("Hello World!"))
  }
  
  def hello = LoggingAction { implicit request =>
    session.get("connected").map { user => 
      Ok("hello " + user)
    }.getOrElse(
      Unauthorized("Oops, you are not connected."))
  }

  def helloWithSession(name: String) = Action {
    Ok("hello " + name).withSession("connected" -> (name + "@gmail.com"))
  }
    
  def helloWorldXml = Action {
    Ok(<message>hello world</message>)
  }  

  def helloWorldHtml = Action {
    Ok(<h1>hello world</h1>).as(HTML)
  }  

  def helloWorldHeaders = Action {
    Ok("Hello world!").withHeaders(CACHE_CONTROL -> "max-age=3600", ETAG -> "xx")
  }  
    
  def echo = LoggingAction { request =>
    Ok("Got request [" + request + "]")
  }

  def echoJson = Action(parse.json) { implicit request =>
    Ok("Got request [" + request + "]")
  }
  
  def pageDetail = LoggingAction { implicit request =>
	  Ok {
	    flash.get("success").getOrElse("Page empty!")
	  }
  }
  
  def show(page: String) = Action {
    Redirect("page/detail").flashing("success" -> ("content for " + page))
  }   
  
  def bye = Action {
    Ok("Bye!").withNewSession
  }
 
}