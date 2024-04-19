package br.dev.rx.conversormoeda.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateAPI {
    private static final String EXCHANGE_API_KEY = "CODIGO DA API AQUI";
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/" + EXCHANGE_API_KEY + "/latest/";

    public double convert(double amount, String currencyBase, String currencyRate) {
        ExchangeRates rates = new ExchangeRates(currencyBase);
        double exchangeRate = rates.getRate(currencyRate);

        return amount * exchangeRate;
    }

    private static class ExchangeRates {
        private final JsonObject currencyRates;

        public ExchangeRates(String currency) {
            this.currencyRates = fetchRates(currency);
        }

        private JsonObject fetchRates(String currency) {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(API_BASE_URL + currency)).build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                    String result = jsonResponse.get("result").getAsString();
                    if (result.equals("success")) {
                        return jsonResponse.getAsJsonObject("conversion_rates");
                    } else if (result.equals("error")) {
                        throw new RuntimeException(jsonResponse.get("error-type").getAsString());
                    } else {
                        throw new RuntimeException(result);
                    }
                } else {
                    throw new RuntimeException("Código de status: " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Erro ao obter a taxa de câmbio", e);
            }
        }

        public double getRate(String currencyCode) {
            if (currencyRates != null && currencyRates.has(currencyCode)) {
                return currencyRates.get(currencyCode).getAsDouble();
            } else {
                return 0;
            }
        }
    }
}
