package dev.sharek.xmla4s.soap

import java.io.{ByteArrayInputStream, StringWriter}

import cats.effect.Concurrent
import javax.xml.parsers.SAXParserFactory
import org.http4s._
import org.http4s.headers.`Content-Type`

import scala.util.control.NonFatal
import scala.xml.{Elem, InputSource, SAXParseException, XML}

trait XmlTranscoders extends Transcoder {
  protected def saxFactory: SAXParserFactory

  implicit def xmlEncoder[F[_]](implicit charset: Charset = Charset.`UTF-8`): EntityEncoder[F, Elem] =
    EntityEncoder
      .stringEncoder[F]
      .contramap[Elem] { node =>
        val sw = new StringWriter
        XML.write(sw, node, charset.nioCharset.name, xmlDecl = true, null)
        sw.toString
      }
      .withContentType(`Content-Type`(MediaType.application.`soap+xml`).withCharset(charset))

  /** Handles a message body as XML.
   * *
   *
   * @return an XML element
   */
  implicit def xmlDecoder[F[_]](implicit F: Concurrent[F]): EntityDecoder[F, Elem] = {
    import EntityDecoder._
    decodeBy(MediaType.text.xml, MediaType.text.html, MediaType.application.xml) { msg =>
      val source: InputSource = new InputSource()
      msg.charset.foreach(cs => source.setEncoding(cs.nioCharset.name))

      collectBinary(msg).flatMap[DecodeFailure, Elem] { chunk =>
        //val chunkWithoutBOM = if (chunk.startsWith(Array[Byte](-17, -69, -65))) chunk.drop(3) else chunk
        // println(chunk.toList.map(_.toChar).mkString)
        source.setByteStream(new ByteArrayInputStream(chunk.toArray))
        val saxParser = saxFactory.newSAXParser()
        try DecodeResult.successT[F, Elem](XML.loadXML(source, saxParser))
        catch {
          case e: SAXParseException =>
            DecodeResult.failureT(MalformedMessageBodyFailure("Invalid XML", Some(e)))
          case NonFatal(e) => DecodeResult(F.raiseError[Either[DecodeFailure, Elem]](e))
        }
      }
    }
  }
}

object XmlTranscoders extends XmlTranscoders {
  override val saxFactory: SAXParserFactory = SAXParserFactory.newInstance
}