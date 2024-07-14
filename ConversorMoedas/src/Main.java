import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        int opcao = 0;
        double valor = 0;
        while (opcao != 7){
            System.out.printf("\n\n Bem vindo ao conversor de moedas, escolha uma das opções a seguir:\n\n");
            System.out.println("1) Converter de Dolar para Real");
            System.out.println("2) Converter de Real para Dolar");
            System.out.println("3) Converter de Real para Euro");
            System.out.println("4) Converter de Euro para Real");
            System.out.println("5) Converter de Peso Argentino para Dolar");
            System.out.println("6) Converter de Real para Peso Argentino");
            System.out.println("7) Sair do Conversor");

            Console c = System.console();
            opcao = Integer.parseInt(c.readLine());

            switch(opcao){
                case 1:
                    System.out.println("Opção 1 - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [USD]: "+ valor);
                    System.out.println("O resultado da conversão é [BRL]: "+ ConverteValor(valor, "USD", "BRL"));
                    break;
                case 2: System.out.println("Opção - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [BRL]: "+ valor);
                    System.out.println("O resultado da conversão é [USD]: "+ ConverteValor(valor, "BRL", "USD"));
                    break;
                case 3: System.out.println("Opção 3 - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [BRL]: "+ valor);
                    System.out.println("O resultado da conversão é [EUR]: "+ ConverteValor(valor, "BRL", "EUR"));
                    break;
                case 4: System.out.println("Opção 4 - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [EUR]: "+ valor);
                    System.out.println("O resultado da conversão é [BRL]: "+ ConverteValor(valor, "EUR", "BRL"));
                    break;
                case 5: System.out.println("Opção 5 - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [ARS]: "+ valor);
                    System.out.println("O resultado da conversão é [USD]: "+ ConverteValor(valor, "ARS", "USD"));
                    break;
                case 6: System.out.println("Opção 6 - Digite o valor:");
                    valor = Double.parseDouble(c.readLine());
                    System.out.println("O valor escolhido foi [BRL]: "+ valor);
                    System.out.println("O resultado da conversão é [ARS]: "+ ConverteValor(valor, "BRL", "ARS"));
                    break;
            }

        }
    }

    private static String ConverteValor(double valor, String moedaOrigem, String moedaDestino) throws IOException {
        String valorConvertido = "";

        // Setting URL
        String url_str = "https://v6.exchangerate-api.com/v6/84f0af4584822143bf2cbb5b/latest/" + moedaOrigem;

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonobj.get("result").getAsString();
        JsonObject conversion_rates = jsonobj.getAsJsonObject("conversion_rates");

        // Verify if request was successful
        if (!"success".equals(req_result)) {
            throw new IOException("Erro ao obter taxas de conversão");
        }

        Gson gson = new Gson();
        TypeToken<Map<String, Double>> mapType = new TypeToken<Map<String, Double>>(){};

        // Deserialization
        Map<String, Double> stringMap = gson.fromJson(conversion_rates, mapType);

        Double conversion_rate = stringMap.get(moedaDestino);
        if (conversion_rate == null) {
            throw new IllegalArgumentException("Moeda de destino não encontrada: " + moedaDestino);
        }

        valorConvertido = String.valueOf(valor * conversion_rate);

        request.disconnect();

        return valorConvertido;
    }
}