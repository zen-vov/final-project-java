package kz.booking.file.service;

import kz.booking.booking.entity.BekbolatovZholamanBooking;
import kz.booking.booking.service.BekbolatovZholamanBookingService;
import kz.booking.common.exception.BekbolatovZholamanBadRequestException;
import kz.booking.file.entity.BekbolatovZholamanStoredFile;
import kz.booking.file.repo.BekbolatovZholamanStoredFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class BekbolatovZholamanFileStorageService {
    private final Path storageDir;
    private final BekbolatovZholamanStoredFileRepository storedFileRepository;
    private final BekbolatovZholamanBookingService bookingService;
    private final BekbolatovZholamanFileScanService fileScanService;

    public BekbolatovZholamanFileStorageService(
            @Value("${app.files.storage-dir}") String storageDir,
            BekbolatovZholamanStoredFileRepository storedFileRepository,
            BekbolatovZholamanBookingService bookingService,
            BekbolatovZholamanFileScanService fileScanService
    ) {
        this.storageDir = Path.of(storageDir);
        this.storedFileRepository = storedFileRepository;
        this.bookingService = bookingService;
        this.fileScanService = fileScanService;
    }

    @Transactional
    public BekbolatovZholamanStoredFile uploadToBooking(Long bookingId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BekbolatovZholamanBadRequestException("File is required");
        }

        BekbolatovZholamanBooking booking = bookingService.getByIdForCurrentUserOrAdmin(bookingId);

        String storageKey = UUID.randomUUID() + "_" + safeName(file.getOriginalFilename());
        try {
            Files.createDirectories(storageDir);
            Path target = storageDir.resolve(storageKey);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BekbolatovZholamanBadRequestException("Failed to store file: " + e.getMessage());
        }

        BekbolatovZholamanStoredFile entity = new BekbolatovZholamanStoredFile(
                booking,
                safeName(file.getOriginalFilename()),
                file.getContentType() == null ? "application/octet-stream" : file.getContentType(),
                file.getSize(),
                storageKey
        );
        BekbolatovZholamanStoredFile saved = storedFileRepository.save(entity);
        fileScanService.scanAsync(saved.getId(), saved.getStorageKey());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<BekbolatovZholamanStoredFile> listByBooking(Long bookingId) {
        bookingService.getByIdForCurrentUserOrAdmin(bookingId);
        return storedFileRepository.findByBookingId(bookingId);
    }

    @Transactional(readOnly = true)
    public DownloadResult download(Long fileId) {
        BekbolatovZholamanStoredFile file = storedFileRepository.findById(fileId)
                .orElseThrow(() -> new kz.booking.common.exception.BekbolatovZholamanNotFoundException("File not found: id=" + fileId));

        bookingService.getByIdForCurrentUserOrAdmin(file.getBooking().getId());
        Path path = storageDir.resolve(file.getStorageKey());
        Resource res = new FileSystemResource(path);
        return new DownloadResult(res, file.getOriginalFilename(), file.getContentType());
    }

    private String safeName(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public record DownloadResult(Resource resource, String filename, String contentType) {
    }
}
