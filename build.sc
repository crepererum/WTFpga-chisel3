import mill._, scalalib._
import coursier.MavenRepository

trait HasChisel3 extends ScalaModule {
  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:3.5.0"
 )
}

trait HasChiselTests extends CrossSbtModule  {
  object test extends Tests {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.10",
      ivy"edu.berkeley.cs::chiseltest:0.5.0"
    )
    def testFramework = "org.scalatest.tools.Framework"
  }
}

trait HasPluginChisel extends ScalaModule {
  val macroPlugins = Agg(ivy"edu.berkeley.cs:::chisel3-plugin:3.5.0")
  def scalacPluginIvyDeps = macroPlugins
  def compileIvyDeps = macroPlugins
}

object WTFpga extends CrossSbtModule with HasChisel3 with HasChiselTests with HasPluginChisel {
  override def millSourcePath = super.millSourcePath
  def crossScalaVersion = "2.13.7"
}
