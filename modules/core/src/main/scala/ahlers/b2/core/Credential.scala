package ahlers.b2.core

import cats.data._
import cats.syntax.option._

/**
 * Contains a key id. and secret used to authorize an account.
 *
 * @see [[https://backblaze.com/b2/docs/b2_authorize_account.html]]
 * @see [[https://backblaze.com/b2/docs/b2_create_key.html]]
 * @see [[https://backblaze.com/b2/docs/b2_delete_key.html]]
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class Credential(key: String, secret: String)
object Credential {

  trait Provider {
    def get(): Option[Credential]
  }

  object Provider {

    /** Always returns the given key and secret. */
    def direct(key: String, secret: String): Provider = Direct(Credential(key, secret))

    /** If both are set, reads a [[Credential]] from the given environment variable names. */
    def environment(key: String, secret: String): Provider = Environment(key, secret)

    case class Direct(credential: Credential) extends Provider {
      override def get() = credential.some
    }

    case class Environment(key: String, secret: String) extends Provider {
      override def get() =
        for {
          k <- sys.env.get(key)
          s <- sys.env.get(secret)
        } yield Credential(k, s)
    }

    case class Chain(providers: NonEmptyList[Provider]) extends Provider {

      /** Returns the first [[Credential]] from all given  */
      override def get() = {
        import util.control.Breaks._
        var c = Option.empty[Credential]
        breakable {
          for (p <- providers.toList) {
            c = p.get()
            if (c.isDefined) break
          }
        }
        c
      }

    }

    object Chain {
      def apply(provider: Provider, providers: Provider*): Chain =
        Chain(NonEmptyList(provider, providers.toList))
    }

    implicit class Ops(val self: Provider) extends AnyVal {

      /** Fall back to the given [[Provider]] if this one produces no [[Credential]]. */
      def |(that: Provider): Provider = Chain(self, that)
    }

  }

}
