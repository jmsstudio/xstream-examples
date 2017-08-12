package br.com.jmsstudio.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.TreeMarshaller;
import org.junit.Assert;
import org.junit.Test;

public class CategoryTest {

    @Test
    public void shouldSerializeCyclicRelationships() {
        Category sport = new Category("sport", null);
        Category soccer = new Category("soccer", sport);
        Category general = new Category("general", soccer);

        //defines cyclic relationship
        sport.setParent(general);

        String expecedXml = "<category id=\"1\">\n" +
                "  <name>sport</name>\n" +
                "  <parent id=\"2\">\n" +
                "    <name>general</name>\n" +
                "    <parent id=\"3\">\n" +
                "      <name>soccer</name>\n" +
                "      <parent reference=\"1\"/>\n" +
                "    </parent>\n" +
                "  </parent>\n" +
                "</category>";

        XStream xStream = new XStream();
        xStream.setMode(XStream.ID_REFERENCES);
        xStream.alias("category", Category.class);

        String generatedXml = xStream.toXML(sport);

        Assert.assertEquals(expecedXml, generatedXml);
    }

    @Test(expected = TreeMarshaller.CircularReferenceException.class)
    public void shouldFailOnSerializingCyclicRelationshipsWithNoReferences() {
        Category sport = new Category("sport", null);
        Category soccer = new Category("soccer", sport);
        Category general = new Category("general", soccer);

        //defines cyclic relationship
        sport.setParent(general);

        XStream xStream = new XStream();
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.alias("category", Category.class);

        xStream.toXML(sport);
    }
}
