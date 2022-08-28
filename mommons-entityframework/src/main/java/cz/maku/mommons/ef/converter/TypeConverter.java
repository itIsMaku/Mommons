package cz.maku.mommons.ef.converter;

public interface TypeConverter<X, Y> {

    Y convertToColumn(X x);

    X convertToEntityField(Y y);

}
