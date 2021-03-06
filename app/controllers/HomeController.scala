package controllers

import javax.inject._

import models.Place
import play.api._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  var places = List(
    Place(1, "Barbosa", Some("Nya")),
    Place(2, "UdeA", Some("Meow")),
    Place(3, "Rionegro", Some("Otro gato")),
    Place(4, "Puto el que lo lea", None)
  )

  def listPlaces = Action{
    val json = Json.toJson(places)
    Ok(json)
  }

  def addPlace = Action{
    request => val json = request.body.asJson.get

    json.validate[Place] match {
      case success: JsSuccess[Place] =>
        places = places :+ success.get
        Ok(Json.toJson(Map("Response" -> "Ingresado correctamente")))
      case error: JsError => BadRequest(Json.toJson(Map("error"->"error")))
    }
  }

  def removePlace (id:Int) = Action{
    places = places.filterNot(_.id == id)
    Ok(Json.toJson(Map("Response" -> "Eliminado correctamente")))
  }

  def updatePlace = Action{
    request => val json = request.body.asJson.get

    json.validate[Place] match{
      case success: JsSuccess[Place] =>
        val newPlace = Place(
          success.get.id,
          success.get.name,
          success.get.description)
        places = places.map(x =>
          if (x.id == success.get.id){
            newPlace
          } else x)
        Ok(Json.toJson(Map("Response" -> "Modificado correctamente")))
      case error: JsError => BadRequest(Json.toJson(Map("error"->"error")))
    }
  }

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
