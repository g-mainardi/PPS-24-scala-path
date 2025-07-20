package it.unibo.controller

/**
 * Trait that manages the simulation speed and delay between steps.
 *
 * [[SpeedManager]] allows the controller to introduce controlled delays
 * between simulation actions, enabling animations or pacing of agent movement.
 *
 * It uses an internal delay multiplier and a flag to determine whether a sleep should occur.
 */
trait SpeedManager:
  private val _delay: Int = 400
  private var _speed: Double = 1.0
  private var _shouldSleep: Boolean = false

  def delay(): Unit =
    if _shouldSleep then
      _shouldSleep = false
      Thread sleep (_delay * _speed).toLong
      
  def shouldSleep(): Unit = _shouldSleep = true

  protected def speed: Double = _speed
  protected def speed_=(newSpeed: Double): Unit = _speed = newSpeed

