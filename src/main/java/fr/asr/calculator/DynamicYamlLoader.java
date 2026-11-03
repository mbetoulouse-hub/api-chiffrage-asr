package fr.asr.calculator;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class DynamicYamlLoader {

    private static final File CONFIGURATION_FOLDER = new File("./configuration/");

    private final ConfigurableEnvironment environment;

    @PostConstruct
    public void loadAllYamls() throws IOException {
        File[] yamls = CONFIGURATION_FOLDER.listFiles((dir, name) -> name.endsWith(".yml"));

        if (yamls != null) {
            YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

            for (File yaml : yamls) {
                List<PropertySource<?>> sources = loader.load(yaml.getName(), new FileSystemResource(yaml));

                for (PropertySource<?> ps : sources) {
                    environment.getPropertySources().addLast(ps);
                }
            }
        }
    }
}
