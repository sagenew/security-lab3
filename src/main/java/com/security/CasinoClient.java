package com.security;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class CasinoClient {
    private static final String CASINO_URL = "http://95.217.177.249/casino";
    private long money;
    public CasinoClient() { }

    public void createAccount(long id) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String uri = CASINO_URL + "/createacc" + "?id=" + id;
        CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
        if (response.getStatusLine().getStatusCode() == 201) {
            money = 1000;
            System.out.println(EntityUtils.toString(response.getEntity()));
        } else {
            throw new RuntimeException();
        }
    }

    public long makeBet(String mode, long playerId, long betMoney,
                        long betNumber, boolean printResponse) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String uri = CASINO_URL + "/play" + mode + "?id=" + playerId
                + "&bet=" + betMoney + "&number=" + betNumber;
        CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
        String entityString = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) {
            if(printResponse) System.out.println(entityString);
            String realNumberString = entityString.substring(
                    entityString.lastIndexOf(":") + 1, entityString.length() - 1);
            long realNumber = Long.parseLong(realNumberString);
            if(realNumber == betNumber) money = money + (1000 * betMoney) - betMoney;
            else money -= betMoney;
            return realNumber;
        } else {
            System.out.println(entityString);
            throw new RuntimeException();
        }
    }

    public long getMoney() {
        return money;
    }
}
