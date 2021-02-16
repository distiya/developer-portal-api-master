package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamAPIAccessItemRequest;
import gov.faa.notam.developerportal.model.api.NotamApiAccessItemModel;
import gov.faa.notam.developerportal.model.api.UpdateNotamApiAccessItemModel;
import gov.faa.notam.developerportal.model.entity.NotamApiAccessItem;
import gov.faa.notam.developerportal.repository.NotamApiAccessItemRepository;
import gov.faa.notam.developerportal.service.NotamApiAccessItemService;
import gov.faa.notam.developerportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotamApiAccessItemServiceImpl implements NotamApiAccessItemService {

    private final ValidationService validationService;
    private final NotamApiAccessItemRepository notamApiAccessItemRepository;

    private final String ERROR_FILE_UPLOAD = "Error in uploading file";
    private final String ERROR_INVALID_ACCESS_ITEM_ID = "Invalid api access item id";
    private final String ERROR_NO_ACCESS_ITEM_FOUND = "Api access item is not found";
    private final String ERROR_NO_VALUES_TO_UPDATE = "At least one value must be provided while updating api access item";

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

    @Override
    public void deleteAccessItem(Long id) throws ApiException {
        validationService.validateAdminAccessRight();
        if(id <= 0){
            throw new ApiException(HttpStatus.BAD_REQUEST,ERROR_INVALID_ACCESS_ITEM_ID);
        }
        boolean isRecordExists = notamApiAccessItemRepository.existsById(id);
        if(Boolean.FALSE.equals(isRecordExists)){
            throw new ApiException(HttpStatus.NOT_FOUND,ERROR_NO_ACCESS_ITEM_FOUND);
        }
        notamApiAccessItemRepository.deleteById(id);
    }

    @Override
    public NotamApiAccessItemModel getAccessItem(Long id) throws ApiException {
        if(id <= 0){
            throw new ApiException(HttpStatus.BAD_REQUEST,ERROR_INVALID_ACCESS_ITEM_ID);
        }
        Optional<NotamApiAccessItem> notamApiAccessItemOptional = notamApiAccessItemRepository.findById(id);
        if(Boolean.TRUE.equals(notamApiAccessItemOptional.isEmpty())){
            throw new ApiException(HttpStatus.NOT_FOUND,ERROR_NO_ACCESS_ITEM_FOUND);
        }
        return new NotamApiAccessItemModel(notamApiAccessItemOptional.get());
    }

    @Override
    public void updateAccessItem(Long id, UpdateNotamApiAccessItemModel request) throws ApiException {
        validationService.validateAdminAccessRight();
        if(Boolean.FALSE.equals(StringUtils.hasText(request.getVersion())) && Boolean.FALSE.equals(StringUtils.hasText(request.getDescription())) && Boolean.FALSE.equals(StringUtils.hasText(request.getChangeLog()))){
            throw new ApiException(HttpStatus.BAD_REQUEST,ERROR_NO_VALUES_TO_UPDATE);
        }
        if(id <= 0){
            throw new ApiException(HttpStatus.BAD_REQUEST,ERROR_INVALID_ACCESS_ITEM_ID);
        }
        Optional<NotamApiAccessItem> notamApiAccessItemOptional = notamApiAccessItemRepository.findById(id);
        if(Boolean.TRUE.equals(notamApiAccessItemOptional.isEmpty())){
            throw new ApiException(HttpStatus.NOT_FOUND,ERROR_NO_ACCESS_ITEM_FOUND);
        }
        NotamApiAccessItem notamApiAccessItem = notamApiAccessItemOptional.orElse(null);
        updateApiAccessItem(notamApiAccessItem,request);
        notamApiAccessItemRepository.save(notamApiAccessItem);
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

    private void updateApiAccessItem(NotamApiAccessItem notamApiAccessItem, UpdateNotamApiAccessItemModel request){
        notamApiAccessItem.setVersion(chooseValueToBeUpdated(request.getVersion(),notamApiAccessItem.getVersion()));
        notamApiAccessItem.setDescription(chooseValueToBeUpdated(request.getDescription(),notamApiAccessItem.getDescription()));
        notamApiAccessItem.setChangeLog(chooseValueToBeUpdated(request.getChangeLog(),notamApiAccessItem.getChangeLog()));
    }

    private <T> T chooseValueToBeUpdated(T newValue, T oldValue){
        if(newValue == null){
            return oldValue;
        }
        if(newValue.getClass().isAssignableFrom(String.class) && Boolean.FALSE.equals(StringUtils.hasText((String) newValue))){
            return oldValue;
        }
        return newValue;
    }

}
