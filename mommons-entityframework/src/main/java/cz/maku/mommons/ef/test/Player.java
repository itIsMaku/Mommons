package cz.maku.mommons.ef.test;

import cz.maku.mommons.ef.annotation.AttributeConvert;
import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity(name = "players")
@Getter
@Setter
public class Player {

    @Id
    @AttributeName(value = "nickname")
    private String name;
    @AttributeConvert(converter = CurrenciesConverter.class)
    private Map<String, Integer> currencies;
}