package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamAPIAccessItemRequest;
import gov.faa.notam.developerportal.model.api.NotamApiAccessItemModel;
import gov.faa.notam.developerportal.model.entity.NotamApiAccessItem;
import gov.faa.notam.developerportal.repository.NotamApiAccessItemRepository;
import gov.faa.notam.developerportal.service.NotamApiAccessItemService;
import gov.faa.notam.developerportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotamApiAccessItemServiceImpl implements NotamApiAccessItemService {

    private final ValidationService validationService;
    private final NotamApiAccessItemRepository notamApiAccessItemRepository;

    private final String ERROR_FILE_UPLOAD = "Error in uploading file";

    @Override
    public NotamApiAccessItemModel createAccessItem(MultipartFile file, CreateNotamAPIAccessItemRequest request) throws ApiException {
        validationService.validateAdminAccessRight();
        validationService.validateBean(request);
        try{
            NotamApiAccessItem notamAPIAccessItem = createNotamAPIAccessItem(file, request);
            notamAPIAccessItem = notamApiAccessItemRepository.save(notamAPIAccessItem);
            return new NotamApiAccessItemModel(notamAPIAccessItem);
        }catch (IOException fileIOException){
            log.error("Error in uploading file : {}",fileIOException.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,ERROR_FILE_UPLOAD);
        }
    }

    private NotamApiAccessItem createNotamAPIAccessItem(MultipartFile file, CreateNotamAPIAccessItemRequest request) throws IOException{
        byte[] fileContentBytes = file.getBytes();
        NotamApiAccessItem notamApiAccessItem = new NotamApiAccessItem();
        notamApiAccessItem.setContent(fileContentBytes);
        notamApiAccessItem.setVersion(request.getVersion());
        notamApiAccessItem.setDescription(request.getDescription());
        notamApiAccessItem.setChangeLog(request.getChangeLog());
        notamApiAccessItem.setType(request.getItemType());
        notamApiAccessItem.setContentType(file.getContentType());
        notamApiAccessItem.setContentLength(fileContentBytes.length);
        notamApiAccessItem.setFileName(file.getOriginalFilename());
        notamApiAccessItem.setDeleted(Boolean.FALSE);
        return notamApiAccessItem;
    }

}
