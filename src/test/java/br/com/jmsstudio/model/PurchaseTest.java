package br.com.jmsstudio.model;

import br.com.jmsstudio.converter.PurchaseDetailsConverter;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PurchaseTest {

    private String getPurchaseXml() {
        String xml = "<purchase>\n" +
                "  <id>1</id>\n" +
                "  <products>\n" +
                "    <product code=\"95\">\n" +
                "      <name>Refrigerator</name>\n" +
                "      <price>123.4</price>\n" +
                "      <full-description>A good refrigerator with freezer</full-description>\n" +
                "    </product>\n" +
                "    <product code=\"88\">\n" +
                "      <name>Microwave</name>\n" +
                "      <price>345.0</price>\n" +
                "      <full-description>A microwave oven</full-description>\n" +
                "    </product>\n" +
                "  </products>\n" +
                "</purchase>";

        return xml;
    }

    private Purchase createPurchaseWithProducts() {
        Product product1 = createRefrigerator();
        Product product2 = createMicrowave();
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        return new Purchase(1, productList);
    }

    private Purchase createPurchaseWithBookAndCellphone() {
        Product product1 = new Book("The shack", 22.9, "A book about religion", 17);
        Product product2 = new Cellphone("Iphone X", 1999.9, "The new Iphone", 228);
        List<Product> productList = new LinkedList<>();
        productList.add(product1);
        productList.add(product2);

        return new Purchase(100, productList);
    }

    private Purchase createPurchaseWithTwoEqualProducts(Product product) {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);

        return new Purchase(1, productList);
    }

    private Product createMicrowave() {
        return new Product("Microwave", 345.0, "A microwave oven", 88);
    }

    private Product createRefrigerator() {
        return new Product("Refrigerator", 123.4, "A good refrigerator with freezer", 95);
    }

    private XStream getXstreamConfig() {
        XStream xStream = new XStream();
        xStream.alias("purchase", Purchase.class);
        xStream.alias("product", Product.class);
        xStream.alias("book", Book.class);
        xStream.alias("cellphone", Cellphone.class);

        xStream.aliasField("full-description", Product.class, "description");
        xStream.useAttributeFor(Product.class, "code");
        return xStream;
    }

    @Test
    public void shouldSerializeAPurchaseToXmlCorrectly() {
        String expectedXml = getPurchaseXml();

        Purchase purchase = createPurchaseWithProducts();

        XStream xStream = getXstreamConfig();

        String xml = xStream.toXML(purchase);

        assertEquals(expectedXml, xml);
    }

    /**
     * In this case, the collection name (products) should not appear in the generated xml.
     */
    @Test
    public void shouldSerializeAPurchaseWithImplictCollectionToXmlCorrectly() {
        String expectedXml = "<purchase>\n" +
                "  <id>1</id>\n" +
                "  <product code=\"95\">\n" +
                "    <name>Refrigerator</name>\n" +
                "    <price>123.4</price>\n" +
                "    <full-description>A good refrigerator with freezer</full-description>\n" +
                "  </product>\n" +
                "  <product code=\"88\">\n" +
                "    <name>Microwave</name>\n" +
                "    <price>345.0</price>\n" +
                "    <full-description>A microwave oven</full-description>\n" +
                "  </product>\n" +
                "</purchase>";

        Purchase purchase = createPurchaseWithProducts();

        XStream xStream = getXstreamConfig();
        xStream.addImplicitArray(Purchase.class, "products");

        String xml = xStream.toXML(purchase);

        assertEquals(expectedXml, xml);
    }

    @Test
    public void shouldDeserializeAnXmlToAPurchaseWithProducts() {
        String originXml = getPurchaseXml();

        XStream xStream = getXstreamConfig();

        Purchase expectedPurchase = createPurchaseWithProducts();

        Purchase purchase = (Purchase) xStream.fromXML(originXml);

        assertEquals(expectedPurchase, purchase);
    }

    @Test
    public void shouldSerializeRepeatedItemsWithNoReferences() {
        String expectedXml = "<purchase>\n" +
                "  <id>1</id>\n" +
                "  <products>\n" +
                "    <product code=\"95\">\n" +
                "      <name>Refrigerator</name>\n" +
                "      <price>123.4</price>\n" +
                "      <full-description>A good refrigerator with freezer</full-description>\n" +
                "    </product>\n" +
                "    <product code=\"95\">\n" +
                "      <name>Refrigerator</name>\n" +
                "      <price>123.4</price>\n" +
                "      <full-description>A good refrigerator with freezer</full-description>\n" +
                "    </product>\n" +
                "  </products>\n" +
                "</purchase>";

        Purchase purchaseWithTwoEqualProducts = createPurchaseWithTwoEqualProducts(createRefrigerator());

        XStream xstream = getXstreamConfig();
        xstream.setMode(XStream.NO_REFERENCES);
        String generatedXml = xstream.toXML(purchaseWithTwoEqualProducts);

        assertEquals(expectedXml, generatedXml);
    }

    @Test
    public void shouldSerializeSubclassesCorrectly() {
        String expectedXml = "<purchase>\n" +
                "  <id>100</id>\n" +
                "  <products class=\"linked-list\">\n" +
                "    <book code=\"17\">\n" +
                "      <name>The shack</name>\n" +
                "      <price>22.9</price>\n" +
                "      <full-description>A book about religion</full-description>\n" +
                "    </book>\n" +
                "    <cellphone code=\"228\">\n" +
                "      <name>Iphone X</name>\n" +
                "      <price>1999.9</price>\n" +
                "      <full-description>The new Iphone</full-description>\n" +
                "    </cellphone>\n" +
                "  </products>\n" +
                "</purchase>";

        Purchase purchaseWithSubclasses = createPurchaseWithBookAndCellphone();
        XStream xStream = getXstreamConfig();

        String generatedXml = xStream.toXML(purchaseWithSubclasses);

        assertEquals(expectedXml, generatedXml);
    }

    @Test
    public void shouldDoSerializationUsingCustomConverter() {
        String expectedXml = "<purchase store=\"store1\">\n" +
                "  <id>1</id>\n" +
                "  <message>Message related to the purchase</message>\n" +
                "  <address>\n" +
                "    <deliveryAddress>\n" +
                "      <street>Av Paulista</street>\n" +
                "      <number>123</number>\n" +
                "      <complement></complement>\n" +
                "      <zipCode>12345-678</zipCode>\n" +
                "      <city>São Paulo</city>\n" +
                "      <state>SP</state>\n" +
                "    </deliveryAddress>\n" +
                "  </address>\n" +
                "  <products>\n" +
                "    <product code=\"95\">\n" +
                "      <name>Refrigerator</name>\n" +
                "      <price>123.4</price>\n" +
                "      <full-description>A good refrigerator with freezer</full-description>\n" +
                "    </product>\n" +
                "    <product code=\"88\">\n" +
                "      <name>Microwave</name>\n" +
                "      <price>345.0</price>\n" +
                "      <full-description>A microwave oven</full-description>\n" +
                "    </product>\n" +
                "  </products>\n" +
                "</purchase>";

        Purchase purchase = createPurchaseWithProducts();

        XStream xStream = getXstreamConfig();
        xStream.registerConverter(new PurchaseDetailsConverter());

        String xml = xStream.toXML(purchase);

        assertEquals(expectedXml, xml);

        Purchase generatedPurchase = (Purchase) xStream.fromXML(xml);

        assertEquals(purchase, generatedPurchase);
    }

}
