package ma.emsi.doctorat.portaildoctorat1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file, Long dossierId) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Verifier extension
        String lowerCaseName = originalFilename.toLowerCase();
        if(!lowerCaseName.endsWith(".pdf") && !lowerCaseName.endsWith(".jpg") && !lowerCaseName.endsWith(".jpeg") && !lowerCaseName.endsWith(".png")) {
            throw new RuntimeException("Seuls les fichiers PDF et images (JPG/PNG) sont autorisés.");
        }
        
        // Verifier taille (5MB)
        if(file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("La taille du fichier ne doit pas dépasser 5MB.");
        }

        try {
            Path targetLocation = Paths.get(uploadDir).resolve(String.valueOf(dossierId));
            Files.createDirectories(targetLocation);
            
            String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = targetLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return String.valueOf(dossierId) + "/" + newFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier " + originalFilename + ". Veuillez réessayer!", ex);
        }
    }

    public Resource loadFileAsResource(String cheminFichier) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(cheminFichier).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Fichier non trouvé " + cheminFichier);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Fichier non trouvé " + cheminFichier, ex);
        }
    }

    public void deleteFile(String cheminFichier) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(cheminFichier).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de supprimer le fichier " + cheminFichier, ex);
        }
    }
}
