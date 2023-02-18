package cz.maku.mommons.ef.test;

import cz.maku.mommons.ef.Repositories;
import cz.maku.mommons.ef.repository.Repository;
import cz.maku.mommons.storage.database.type.MySQL;
import cz.maku.mommons.worker.Worker;
import lombok.SneakyThrows;

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
        player.setPlayerName("kokot");
        player.setCurrencies(new HashMap<>());
        player.setRank("admin");
        repository.createOrUpdate(player);

        Player kokot = repository.select("kokot");
        if (kokot != null) {
            kokot.setRank("user");
            repository.update(kokot);
        }
    }

}