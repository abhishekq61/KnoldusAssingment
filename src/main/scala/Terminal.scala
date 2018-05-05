import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class Terminal {
  val products = new scala.collection.mutable.ListBuffer[Product]
  val orderCart = new scala.collection.mutable.ListBuffer[Product]
  val logger = Logger(LoggerFactory.getLogger("Termnail Logger"))

  def addProduct(product: Product): Unit = {
    this.products += product
  }

  //Higher order func Example.
  def setPricing(productCode: String, f: Price => Price): Boolean = {
    this.products.zipWithIndex.filter(_._1.code == productCode) match {
      case y if y.nonEmpty => y.foreach { x =>
        this.products.update(x._2, Product(productCode, f(x._1.price)))
      }
        true
      case _ => logger.warn(s"Specified ProductCode : ${productCode} Doesn't Exist,Can't Assign Price")
        false
    }


  }

  def scan(productCode: String): Unit = {
    products.find(_.code == productCode) match {
      case Some(product) => orderCart += product
      case _ => logger.warn("Scanned Product Doesn't Exist In Store")
    }
  }

  def total: Double = {
    val result = orderCart.groupBy(_.code).foldLeft(0.0) {
      (productCode, products) =>
        if (products._2.head.price.volumePrice.units > 0 && products._2.size / products._2.head.price.volumePrice.units > 0) {
          val noOfPacks = products._2.size / products._2.head.price.volumePrice.units
          productCode + noOfPacks * products._2.head.price.volumePrice.price + (products._2.size - noOfPacks * products._2.head.price.volumePrice.units) * products._2.head.price.unitPrice.price
        }
        else
          productCode + products._2.size * products._2.head.price.unitPrice.price
    }
    this.orderCart.clear()
    result
  }

}

case class Product
(
  code: String,
  price: Price
)

object Product {
  def apply(code: String): Product = {
    Product(code, Price(UnitPrice(), VolumePrice()))
  }
}

case class Price
(
  unitPrice: UnitPrice,
  volumePrice: VolumePrice
)

case class UnitPrice
(
  price: Double = 0.0
)

case class VolumePrice
(
  units: Int = 0,
  price: Double = 0.0

)