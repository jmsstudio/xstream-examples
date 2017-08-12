package br.com.jmsstudio.model;

import com.thoughtworks.xstream.XStream;
import org.junit.Assert;
import org.junit.Test;

public class ProductTest {

    @Test
    public void shouldCreateXmlCorrectly() {
        String expectedXml = "<product code=\"95\">\n" +
                "  <name>Refrigerator</name>\n" +
                "  <price>123.4</price>\n" +
                "  <full-description>A good refrigerator with freezer</full-description>\n" +
                "</product>";

        Product product = new Product("Refrigerator", 123.4, "A good refrigerator with freezer", 95);

        XStream xStream = new XStream();
        xStream.alias("product", Product.class);
        xStream.aliasField("full-description", Product.class, "description");
        xStream.useAttributeFor(Product.class, "code");

        String xml = xStream.toXML(product);

        Assert.assertEquals(expectedXml, xml);
    }
}
