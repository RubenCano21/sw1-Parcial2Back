package bo.edu.uagrm.sw1parcial2.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FlutterService {

    public byte[] generateFlutterZip(String flutterCode) throws IOException {
        // 1. Crear un directorio temporal
        Path tempDir = Files.createTempDirectory("flutter_project");

        try {
            // 2. Crear la estructura de carpetas y archivos necesarios
            Path libDir = tempDir.resolve("lib");
            Files.createDirectories(libDir);

            // 3. Crear el archivo main.dart con el código proporcionado
            Path mainDart = libDir.resolve("main.dart");
            Files.writeString(mainDart, flutterCode);

            // 4. Crear un archivo pubspec.yaml básico
            String pubspec = """
                    name: generated_ui
                    description: A generated Flutter UI
                    publish_to: 'none'
    
                    environment:
                      sdk: '>=3.0.0 <4.0.0'
    
                    dependencies:
                      flutter:
                        sdk: flutter
    
                    dev_dependencies:
                      flutter_test:
                        sdk: flutter
    
                    flutter:
                      uses-material-design: true
                    """;
            Files.writeString(tempDir.resolve("pubspec.yaml"), pubspec);

            // 5. .gitignore file
            Files.writeString(tempDir.resolve(".gitignore"), "/build/\n.dart_tool/\n.packages");

            // 6. Crear un archivo zip del directorio
            Path zipPath = Files.createTempFile("flutter_project", ".zip");
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                Files.walk(tempDir)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            try {
                                String zipEntry = tempDir.relativize(path).toString().replace("\\", "/");
                                zos.putNextEntry(new ZipEntry(zipEntry));
                                Files.copy(path, zos);
                                zos.closeEntry();
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }
            // 7. Leer el archivo zip y devolverlo como byte array
            return Files.readAllBytes(zipPath);
        } finally {
            // Limpieza: eliminar el directorio temporal y sus archivos
            try {
                Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a)) // Eliminar archivos antes que directorios
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {}
                    });
            } catch (IOException ignored) {}
        }
    }

}
