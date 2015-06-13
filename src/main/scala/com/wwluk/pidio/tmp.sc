import scala.language.implicitConversions
import scala.util.{Failure, Try}

val s = """>>> POLSKASTACJA .PL >>> - HOT 100 - Goraca Setka Nowych HITOW (Polskie Radio): Aronchupa - I'm An Albatraoz
          |[playing] #3/3   0:50/0:00 (0%)
          |volume: 90%   repeat: off   random: off   single: off   consume: off"""
val wrongValue: String = "asdasdasd"

val r = """volume: (\d+)%""".r
r.findFirstMatchIn(s).get.group(1).toInt


def volume(s: String): Int = r.findFirstMatchIn(s).fold(0)(_.group(1).toInt)
def volumeOpt(s: String): Option[Int] = r.findFirstMatchIn(s).fold[Option[Int]](None)(m => Some(m.group(1).toInt))
def volumeOpt2(s: String): Option[Int] = r.findFirstMatchIn(s).map(_.group(1).toInt)

volume(s)
volume(wrongValue)

volumeOpt(s)
volumeOpt(wrongValue)

volumeOpt2(s)
volumeOpt2(wrongValue)


val x = """repeat: (\w+)""".r.findFirstMatchIn(s).map(_.group(1))
val xB = Try(strToBoolan(x.getOrElse("off")))



implicit def strToBoolan(s: String): Boolean = s match {
  case "off" => false
  case "on" => true
}

/*
val a = 1 match {
case 2 => false
case 3 => true
}

println(a) */
