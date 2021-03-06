package ahlers.b2.api.v2

/**
 * @see [[https://backblaze.com/b2/docs/b2_list_buckets.html]]
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ListBuckets(
  accountId: AccountId,
  bucketId: Option[BucketId],
  bucketName: Option[BucketName],
  bucketTypes: Option[Seq[BucketType]])
