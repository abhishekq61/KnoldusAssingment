import org.specs2.mutable
import org.specs2.mutable.Specification

class TerminalCheckoutSpec extends Specification {
  def getTerminal: Terminal = {
    val terminal = new Terminal()
    terminal.addProduct(Product("A"))
    terminal.addProduct(Product("B"))
    terminal.addProduct(Product("C"))
    terminal.addProduct(Product("D"))
    //Updating only unitPrice
    terminal.setPricing("A", x => {
      x.copy(unitPrice = UnitPrice(2.0))
    })
    //Updating only volumePrice
    terminal.setPricing("A", x => {
      x.copy(volumePrice = VolumePrice(4, 7.0))
    })
    //Updating both unit and volume price
    terminal.setPricing("B", _ => {
      Price(UnitPrice(12), VolumePrice(0, 0.0))
    })
    terminal.setPricing("C", _ => {
      Price(UnitPrice(1.25), VolumePrice(6, 6.0))
    })
    terminal.setPricing("D", _ => {
      Price(UnitPrice(.15), VolumePrice(0, 0))
    })
    terminal
  }

  "Terminal" should {
    "charge 32.40 for ABCDABAA" in {
      val terminal = getTerminal
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("C")
      terminal.scan("D")
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("A")
      terminal.scan("A")
      val result = terminal.total
      result must_== 32.40
    }

    "charge 7.25 for CCCCCCC" in {
      val terminal = getTerminal
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      val result = terminal.total
      result must_== 7.25
    }
    "charge 15.40 for ABCD" in {
      val terminal = getTerminal
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("C")
      terminal.scan("D")
      val result = terminal.total
      result must_== 15.40
    }
    //Additional test cases
    "clear shopping cart after each total" in {
      val terminal = getTerminal
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("C")
      terminal.scan("D")
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("A")
      terminal.scan("A")
      val result = terminal.total
      result must_== 32.40
      terminal.total must_== 0.0
    }

  }
}
