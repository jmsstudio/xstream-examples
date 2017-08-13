package br.com.jmsstudio.converter;

import br.com.jmsstudio.model.Product;
import br.com.jmsstudio.model.Purchase;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailsConverter implements Converter {
    @Override
    public boolean canConvert(Class type) {
        return type.isAssignableFrom(Purchase.class);
    }

    /**
     * Serializes the Purchase
     * @param purchase
     * @param writer
     * @param marshallingContext
     */
    @Override
    public void marshal(Object purchase, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        Purchase purchaseToBeMarshalled = (Purchase) purchase;

        writer.addAttribute("store", "store1");

        writer.startNode("id");
        marshallingContext.convertAnother(purchaseToBeMarshalled.getId());
        writer.endNode();

        writer.startNode("message");
        writer.setValue("Message related to the purchase");
        writer.endNode();

        writer.startNode("address");
            writer.startNode("deliveryAddress");
                writer.startNode("street");
                    writer.setValue("Av Paulista");
                writer.endNode();
                writer.startNode("number");
                    writer.setValue("123");
                writer.endNode();
                writer.startNode("complement");
                    writer.setValue("");
                writer.endNode();
                writer.startNode("zipCode");
                    writer.setValue("12345-678");
                writer.endNode();
                writer.startNode("city");
                    writer.setValue("São Paulo");
                writer.endNode();
                writer.startNode("state");
                    writer.setValue("SP");
                writer.endNode();
            writer.endNode();
        writer.endNode();
        writer.startNode("products");
            marshallingContext.convertAnother(purchaseToBeMarshalled.getProducts());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        String store = reader.getAttribute("store");
        System.out.println("store: " + store);

        reader.moveDown();
        String idNodeName = reader.getNodeName();
        Integer idValue = Integer.parseInt(reader.getValue());
        reader.moveUp();

        System.out.println("Campo: " + idNodeName + " - Valor: " + idValue);

        //reads the message
        reader.moveDown();
        String messageNodeName = reader.getNodeName();
        String messageValue = reader.getValue();
        reader.moveUp();

        System.out.println("Campo: " + messageNodeName + " - Valor: " + messageValue);

        //reads the address
        reader.moveDown();
            reader.moveDown();
                reader.moveDown();
                String streetNodeName = reader.getNodeName();
                String streetValue = reader.getValue();

                System.out.println("Campo: " + streetNodeName + " - Valor: " + streetValue);
                reader.moveUp();

                reader.moveDown();
                String number = reader.getNodeName();
                String numberValue = reader.getValue();

                System.out.println("Campo: " + number + " - Valor: " + numberValue);
                reader.moveUp();

                //complement
                reader.moveDown();
                reader.moveUp();

                //zipCode
                reader.moveDown();
                reader.moveUp();

                //city
                reader.moveDown();
                reader.moveUp();

                //state
                reader.moveDown();
                String state = reader.getNodeName();
                String stateValue = reader.getValue();

                System.out.println("Campo: " + state + " - Valor: " + stateValue);
                reader.moveUp();
            reader.moveUp();
        reader.moveUp();

        List<Product> productList = new ArrayList<>();
        Purchase purchase = new Purchase(idValue, productList);

        //products
        reader.moveDown();

        List<Product> deserializedProducts = (List<Product>) unmarshallingContext.convertAnother(purchase, List.class);
        productList.addAll(deserializedProducts);
        reader.moveUp();

        return purchase;
    }

}
