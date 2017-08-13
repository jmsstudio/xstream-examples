package br.com.jmsstudio.converter;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class PriceConverter implements SingleValueConverter {
    @Override
    public String toString(Object value) {
        Double price = (Double) value;
        NumberFormat formatter = getNumberFormat();

        return formatter.format(price);
    }

    @Override
    public Object fromString(String textValue) {
        Number parsedValue = null;
        try {
            parsedValue = getNumberFormat().parse(textValue);
        } catch (ParseException e) {
            throw new ConversionException("Problem converting value: " + textValue, e);
        }

        return parsedValue;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.isAssignableFrom(Double.class);
    }

    private NumberFormat getNumberFormat() {
        Locale brazil = new Locale("pt", "br");
        return NumberFormat.getCurrencyInstance(brazil);
    }

}
