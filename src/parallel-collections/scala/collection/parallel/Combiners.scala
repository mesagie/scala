package scala.collection.parallel


import scala.collection.Parallel
import scala.collection.mutable.Builder
import scala.collection.generic.Sizing



/** The base trait for all combiners.
 *  A combiner lets one construct collections incrementally just like
 *  a regular builder, but also implements an efficient merge operation of two builders
 *  via `combine` method. Once the collection is constructed, it may be obtained by invoking
 *  the `result` method.
 *
 *  @tparam Elem   the type of the elements added to the builder
 *  @tparam To     the type of the collection the builder produces
 *
 *  @author prokopec
 */
trait Combiner[-Elem, +To] extends Builder[Elem, To] with Sizing with Parallel with TaskSupport {
  self: EnvironmentPassingCombiner[Elem, To] =>

  type EPC = EnvironmentPassingCombiner[Elem, To]

  /** Combines the contents of the receiver builder and the `other` builder,
   *  producing a new builder containing both their elements.
   *
   *  This method may combine the two builders by copying them into a larger collection,
   *  by producing a lazy view that gets evaluated once `result` is invoked, or use
   *  a merge operation specific to the data structure in question.
   *
   *  Note that both the receiver builder and `other` builder become invalidated
   *  after the invocation of this method, and should be cleared (see `clear`)
   *  if they are to be used again.
   *
   *  @tparam N      the type of elements contained by the `other` builder
   *  @tparam NewTo  the type of collection produced by the `other` builder
   *  @param other   the other builder
   *  @return        the parallel builder containing both the elements of this and the `other` builder
   */
  def combine[N <: Elem, NewTo >: To](other: Combiner[N, NewTo]): Combiner[N, NewTo]

}


trait EnvironmentPassingCombiner[-Elem, +To] extends Combiner[Elem, To] {
  abstract override def result = {
    val res = super.result
//    res.environment = environment
    res
  }
}









