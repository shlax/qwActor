package org.qwActor.stream.iterator

import org.qwActor.{ActorMessage, ActorRef}
import org.qwActor.stream.iterator.messages.{End, HasNext, Next, IteratorMessage}
import java.util.concurrent.locks.ReentrantLock

object ActorSource{

  def apply[A](it:Source[A]):ActorSource[A] = new ActorSource(it)

}

/** Iterator will be called from multiple threads */
class ActorSource[A](it:Source[A]) extends ActorRef {

  private val lock = new ReentrantLock

  override def accept(t: ActorMessage): Unit = {
    t.value match {
      case HasNext =>
        val v : IteratorMessage = try {
          lock.lock()
          it.next() match {
            case Some(i) => Next(i)
            case None => End
          }
        }finally {
          lock.unlock()
        }
        t.sender << v
    }
  }

}
