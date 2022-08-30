package cz.maku.mommons.ef.test;

import com.google.common.collect.Maps;
import cz.maku.mommons.ef.Repositories;
import cz.maku.mommons.ef.repository.DefaultRepository;
import cz.maku.mommons.ef.repository.Repository;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.Worker;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.util.HashMap;

public class Test {

    @SneakyThrows
    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.setPublicMySQL(new MySQL("127.0.0.1", 3306, "mommons", "root", "", false, true));
        worker.initialize();
        MySQL.getApi().connect();


        Repository<String, Player> repository = Repositories.createRepository(MySQL.getApi().getConnection(), Player.class);
        Player player = new Player();
        player.setName("kokot");
        player.setCurrencies(new HashMap<>());

        repository.createOrUpdate(player);
    }

}