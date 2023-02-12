package cz.maku.mommons.ef.test;

import cz.maku.mommons.ef.annotation.AttributeConvert;
import cz.maku.mommons.ef.annotation.AttributeName;
import cz.maku.mommons.ef.annotation.Entity;
import cz.maku.mommons.ef.annotation.Id;
import cz.maku.mommons.ef.entity.NamePolicy;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity(name = "players", namePolicy = NamePolicy.JAVA)
@Getter
@Setter
public class Player {

    @Id
    private String playerName;
    @AttributeConvert(converter = CurrenciesConverter.class)
    private Map<String, Integer> currencies;
    private String rank;
}