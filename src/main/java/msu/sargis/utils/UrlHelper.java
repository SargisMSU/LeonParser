package msu.sargis.utils;

public class UrlHelper {
    private final static String leaguesUrl = "https://leon.bet/api-2/betline/sports?ctag=ru-RU&flags=urlv2";

    public static String getLeaguesUrl() {
        return leaguesUrl;
    }

    public static String getLeagueUrl(Long id){
        return "https://leon.bet/api-2/betline/events/all?ctag=ru-RU&flags=reg,mm2,rrc,nodup,urlv2,smg,outv2&league_id=" + id;
    }

    public static String getGameUrl(Long id){
        return "https://leon.bet/api-2/betline/event/all?ctag=ru-RU&flags=reg,mm2,rrc,nodup,urlv2,smg,outv2&eventId=" + id;
    }
}
