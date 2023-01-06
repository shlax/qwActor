package org.qwActor

import org.qwActor.{Actor, ActorContext, ActorMessage, ActorRef}

import java.util.ArrayList
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.*

object ActorSystem{
  def apply():ActorSystem = new ActorSystem
}

class ActorSystem extends ActorRuntime, Executor, AutoCloseable{

  override def create(fn: ActorContext => Actor): ActorRef = new ActorState(this, fn)

  protected def createExecutor(): ExecutorService = Executors.newWorkStealingPool()
  private val executor = createExecutor()

  override def execute(actorState: Runnable): Unit = {
    executor.execute(actorState)
  }

  override def close(): Unit = {
    if(!executor.isTerminated) executor.shutdown()
  }

}
