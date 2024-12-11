package br.com.nakatec.mapa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Configuration
public class YamlPropertySourceFactory {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigInDev(ConfigurableEnvironment environment) throws IOException {
        // Carregar o arquivo apiKey.yml da raiz do projeto
        Yaml yaml = new Yaml();
        FileInputStream inputStream = new FileInputStream("apiKey.yml");
        Map<String, Object> obj = yaml.load(inputStream);

        // Verifique se o arquivo contém a chave esperada antes de tentar acessá-la
        if (obj != null && obj.containsKey("api")) {
            Map<String, Object> apiMap = (Map<String, Object>) obj.get("api");

            if (apiMap != null && apiMap.containsKey("key")) {
                Map<String, Object> keyMap = (Map<String, Object>) apiMap.get("key");

                // Verifique se a chave API_KEY existe e não é nula
                if (keyMap != null && keyMap.containsKey("API_KEY")) {
                    String apiKeyFromYaml = (String) keyMap.get("API_KEY");

                    // Registrar a chave no ambiente
                    environment.getPropertySources().addFirst(
                            new MapPropertySource("apiKeySource", Map.of("api.key.API_KEY", apiKeyFromYaml))
                    );

                    // Imprimir a chave carregada para confirmação
                    System.out.println("Chave API carregada: " + apiKeyFromYaml);
                } else {
                    throw new IllegalStateException("API_KEY não encontrada no arquivo YAML.");
                }
            } else {
                throw new IllegalStateException("Chave 'key' não encontrada no arquivo YAML.");
            }
        } else {
            throw new IllegalStateException("Chave 'api' não encontrada no arquivo YAML.");
        }

        return new PropertySourcesPlaceholderConfigurer();
    }
}
