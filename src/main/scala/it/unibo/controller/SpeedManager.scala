package it.unibo.controller

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

