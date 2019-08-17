package ahlers.b2.api.v2

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class Bucket(
    accountId: String,
    bucketId: String,
    bucketName: String,
    bucketType: BucketType,
    bucketInfo: Map[String, String],
    corsRules: Seq[CorsRule],
    lifecycleRules: Seq[LifecycleRule],
    revision: Long
)
