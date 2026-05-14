package kz.booking.file.controller;

import kz.booking.file.dto.BekbolatovZholamanStoredFileResponse;
import kz.booking.file.mapper.BekbolatovZholamanStoredFileMapper;
import kz.booking.file.service.BekbolatovZholamanFileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BekbolatovZholamanFileController {
    private final BekbolatovZholamanFileStorageService fileStorageService;

    public BekbolatovZholamanFileController(BekbolatovZholamanFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/bookings/{bookingId}/files")
    public BekbolatovZholamanStoredFileResponse upload(@PathVariable Long bookingId, @RequestParam("file") MultipartFile file) {
        return BekbolatovZholamanStoredFileMapper.toResponse(fileStorageService.uploadToBooking(bookingId, file));
    }

    @GetMapping("/bookings/{bookingId}/files")
    public List<BekbolatovZholamanStoredFileResponse> list(@PathVariable Long bookingId) {
        return fileStorageService.listByBooking(bookingId).stream().map(BekbolatovZholamanStoredFileMapper::toResponse).toList();
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        BekbolatovZholamanFileStorageService.DownloadResult d = fileStorageService.download(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(d.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + d.filename() + "\"")
                .body(d.resource());
    }
}

