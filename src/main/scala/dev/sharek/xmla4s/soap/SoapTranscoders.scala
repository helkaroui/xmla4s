package dev.sharek.xmla4s.soap

import cats.effect.Concurrent
import org.http4s._
import org.http4s.headers.`Content-Type`

import scala.util.control.NonFatal
import scala.xml.{Elem, NodeSeq}

trait SoapTranscoders extends Transcoder {

  implicit def soapEncoder[F[_]](implicit charset: Charset = Charset.`UTF-8`): EntityEncoder[F, SoapRequest] =
    XmlTranscoders.xmlEncoder[F]
      .contramap[SoapRequest] {
        req =>
          <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:m="http://www.example.org">
            {wrapHeaders(req.headers)}{wrapBody(req.body)}
          </soap:Envelope>
      }
      .withContentType(`Content-Type`(MediaType.application.`soap+xml`).withCharset(charset))

  private def wrapBody(body: NodeSeq): Elem =
    <soap:Body>
      {body}
    </soap:Body>

  private def wrapHeaders(headers: NodeSeq): Elem =
    <soap:Header>
      {headers}
    </soap:Header>

  private def parseEnvelope(elm: Elem): SoapResponse = {
    val headers = (elm \\ "Envelope" \\ "Header").headOption
    val body = (elm \\ "Envelope" \\ "Body").headOption

    (headers, body) match {
      case (Some(hdr), Some(bd)) => SoapResponse(hdr.child, bd.child)
      case (None, _) => throw new SoapParseException("Invalid Soap : Header section is missing")
      case (_, None) => throw new SoapParseException("Invalid Soap : Body section is missing")
    }

  }

  implicit def SoapDecoder[F[_]](implicit F: Concurrent[F]): EntityDecoder[F, SoapResponse] =
    XmlTranscoders.xmlDecoder[F]
      .flatMapR[SoapResponse] { elm: Elem =>
        try DecodeResult.successT[F, SoapResponse](parseEnvelope(elm))
        catch {
          case e: Exception =>
            DecodeResult.failureT(MalformedMessageBodyFailure("Invalid Soap", Some(e)))
          case NonFatal(e) => DecodeResult(F.raiseError[Either[DecodeFailure, SoapResponse]](e))
        }
      }

}


object SoapTranscoders extends SoapTranscoders