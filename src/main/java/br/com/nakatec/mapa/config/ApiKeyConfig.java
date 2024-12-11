package br.com.nakatec.mapa.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Configuration
public class ApiKeyConfig {

    // Essa propriedade será injetada com o valor do arquivo 'apiKey.yml'
    @Value("${api.key.API_KEY}")
    private String apiKey;

    @Bean
    public CommandLineRunner loadApiKey(ConfigurableEnvironment environment) throws IOException {
        return args -> {
            // Carregar o arquivo apiKey.yml da raiz do projeto
            Yaml yaml = new Yaml();
            FileInputStream inputStream = new FileInputStream("apiKey.yml");
            Map<String, Object> obj = yaml.load(inputStream);

            // Registrar o valor da chave API_KEY como uma propriedade
            Map<String, Object> apiKeyMap = (Map<String, Object>) obj.get("api");
            Map<String, Object> keyMap = (Map<String, Object>) apiKeyMap.get("key");

            // Registrar a chave como uma propriedade no ambiente Spring
            String apiKeyFromYaml = (String) keyMap.get("API_KEY");

            // Usando environment.addPropertySource para adicionar a chave ao ambiente
            environment.getPropertySources().addFirst(
                    new org.springframework.core.env.MapPropertySource("apiKeySource", Map.of("api.key.API_KEY", apiKeyFromYaml))
            );

            // Imprimir a chave carregada para confirmação
            System.out.println("Chave API carregada: " + apiKeyFromYaml);
        };
    }

    public String getApiKey() {
        return apiKey;
    }
}
