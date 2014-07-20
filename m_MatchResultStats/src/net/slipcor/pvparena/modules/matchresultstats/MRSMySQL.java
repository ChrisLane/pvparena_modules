package net.slipcor.pvparena.modules.matchresultstats;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * stats access class
 *
 * @author slipcor
 * @version v0.1.0
 */

final class MRSMySQL {

    private MRSMySQL() {

    }

    private static MatchResultStats plugin = null;

    private static void mysqlQuery(final String query) {
        try {
            plugin.sqlHandler.executeQuery(query, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void initiate(final MatchResultStats pvpStats) {
        plugin = pvpStats;
    }

    /*
        private static void wipe(final String name) {
            if (name == null) {
                mysqlQuery("DELETE FROM `"+MatchResultStats.dbTable+"` WHERE 1;");
            } else {/*
                PVPData.setDeaths(name, 0);
                PVPData.setKills(name, 0);
                PVPData.setMaxStreak(name, 0);
                PVPData.setStreak(name, 0); * /

                mysqlQuery("DELETE FROM `"+MatchResultStats.dbTable+"` WHERE `name` = '" + name
                        + "';");
            }
        }
    */
    public static void save(int id, String arenaName, String playerName, boolean winning,
                            String team, long time) {

        mysqlQuery("INSERT INTO `" + MatchResultStats.dbTable + "` (`mid`,`arena`,`playername`,`winning`,`team`,`timespent`) VALUES ('"
                + id + "', '" + arenaName + "', '" + playerName + "', " + (winning ? "1" : "0") + ", '" + team + "', " + time + ")");
    }

    public static Integer getNextID() {
        int number = -1;

        try {
            ResultSet result = null;
            try {
                result = plugin.sqlHandler
                        .executeQuery("SELECT `mid` FROM `" + MatchResultStats.dbTable + "` WHERE 1;", false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                while (result != null && result.next()) {
                    number = result.getInt("mid");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
        return ++number;
    }
}
