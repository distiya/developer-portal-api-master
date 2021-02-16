package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.model.entity.NotamApiAccessItem;
import gov.faa.notam.developerportal.repository.NotamApiAccessItemRepository;
import gov.faa.notam.developerportal.service.NotamApiAccessItemService;
import gov.faa.notam.developerportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotamApiAccessItemServiceImpl implements NotamApiAccessItemService {

    private final ValidationService validationService;
    private final NotamApiAccessItemRepository notamApiAccessItemRepository;
    private final EntityManager entityManager;

    private final String ERROR_FILE_UPLOAD = "Error in uploading file";
    private final String ERROR_INVALID_ACCESS_ITEM_ID = "Invalid api access item id";
    private final String ERROR_NO_ACCESS_ITEM_FOUND = "Api access item is not found";
    private final String ERROR_NO_VALUES_TO_UPDATE = "At least one value must be provided while updating api access item";

    @Value("${pagination.limit.default}")
    private Integer defaultPageLimit;

    @Value("${pagination.limit.max}")
    private Integer maxPageLimit;

    @Value("${api.access.item.sortable.fields}")
    private String sortableFields;

    @Value("${api.access.item.sortable.fields.mapping}")
    private String sortableFieldsMapping;

    private Set<String> sortableFieldsSet = new HashSet<>();
    private Map<String,String> sortableFieldsMappingDictionary = new HashMap<>();

    private final String MAPPING_KEY_SEP = "#";
    private final String MAPPING_ENTRY_SEP = ",";

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
    public byte[] getAccessItemFile(Long id) throws ApiException {
        if(id <= 0){
            throw new ApiException(MediaType.APPLICATION_OCTET_STREAM,HttpStatus.BAD_REQUEST,ERROR_INVALID_ACCESS_ITEM_ID);
        }
        Optional<NotamApiAccessItem> notamApiAccessItemOptional = notamApiAccessItemRepository.findById(id);
        if(Boolean.TRUE.equals(notamApiAccessItemOptional.isEmpty())){
            throw new ApiException(MediaType.APPLICATION_OCTET_STREAM,HttpStatus.NOT_FOUND,ERROR_NO_ACCESS_ITEM_FOUND);
        }
        return  notamApiAccessItemOptional.get().getContent();
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
        NotamApiAccessItem notamApiAccessItem = notamApiAccessItemOptional.get();
        updateApiAccessItem(notamApiAccessItem,request);
        notamApiAccessItemRepository.save(notamApiAccessItem);
    }

    @Override
    public NotamApiAccessItemModelSearchResult searchAccessItem(SearchNotamApiAccessItemRequest request) throws ApiException {
        if(Boolean.TRUE.equals(CollectionUtils.isEmpty(sortableFieldsSet))){
            Arrays.stream(sortableFieldsMapping.split(MAPPING_ENTRY_SEP)).forEach(entry->{
                String[] entrySep = entry.split(MAPPING_KEY_SEP);
                sortableFieldsMappingDictionary.put(entrySep[0],entrySep[1]);
            });
            sortableFieldsSet.addAll(Arrays.asList(sortableFields.split(MAPPING_ENTRY_SEP)));
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotamApiAccessItemModel> criteriaQuery = criteriaBuilder.createQuery(NotamApiAccessItemModel.class);
        Root<NotamApiAccessItem> apiAccessItemRoot = criteriaQuery.from(NotamApiAccessItem.class);
        CompoundSelection<NotamApiAccessItemModel> compoundSelection = criteriaBuilder.construct(NotamApiAccessItemModel.class, apiAccessItemRoot.get("id"),apiAccessItemRoot.get("type"),apiAccessItemRoot.get("version"),apiAccessItemRoot.get("description"),apiAccessItemRoot.get("changeLog"),apiAccessItemRoot.get("contentType"),apiAccessItemRoot.get("contentLength"),apiAccessItemRoot.get("fileName"),apiAccessItemRoot.get("createdAt"),apiAccessItemRoot.get("updatedAt"));
        List<Predicate> queryPredicates = new ArrayList<>();
        List<Order> orderList = new ArrayList();
        if(Boolean.TRUE.equals(StringUtils.hasText(request.getKeyword()))){
            Predicate descriptionPredicate = criteriaBuilder.like(apiAccessItemRoot.get("description"), "%" + request.getKeyword() + "%");
            Predicate changeLogPredicate = criteriaBuilder.like(apiAccessItemRoot.get("changeLog"), "%" + request.getKeyword() + "%");
            Predicate versionPredicate = criteriaBuilder.like(apiAccessItemRoot.get("version"), "%" + request.getKeyword() + "%");
            Predicate keyWordPredicate = criteriaBuilder.or(descriptionPredicate, changeLogPredicate, versionPredicate);
            queryPredicates.add(keyWordPredicate);
        }
        if(request.getItemType() != null){
            Predicate itemTypePredicate = criteriaBuilder.equal(apiAccessItemRoot.get("type"), request.getItemType());
            queryPredicates.add(itemTypePredicate);
        }
        if(Boolean.FALSE.equals(CollectionUtils.isEmpty(queryPredicates))){
            criteriaQuery.where(queryPredicates.toArray(new Predicate[0]));
        }
        criteriaQuery.select(compoundSelection);
        if(Boolean.TRUE.equals(StringUtils.hasText(request.getSortBy())) && sortableFieldsSet.contains(request.getSortBy()) && request.getSortOrder() != null){
            if(SortOrder.Asc.equals(request.getSortOrder())){
                orderList.add(criteriaBuilder.asc(apiAccessItemRoot.get(getSortableColumnName(request.getSortBy()))));
            }
            else{
                orderList.add(criteriaBuilder.desc(apiAccessItemRoot.get(getSortableColumnName(request.getSortBy()))));
            }
        }
        if(Boolean.FALSE.equals(CollectionUtils.isEmpty(orderList))){
            criteriaQuery.orderBy(orderList);
        }
        if(request.getOffset() == null){
            request.setOffset(0);
        }
        if(request.getLimit() == null){
            request.setLimit(defaultPageLimit);
        }
        else if(request.getLimit() > maxPageLimit){
            request.setLimit(maxPageLimit);
        }
        List<NotamApiAccessItemModel> resultList = entityManager.createQuery(criteriaQuery).setFirstResult(request.getOffset()).setMaxResults(request.getLimit()).getResultList();
        NotamApiAccessItemModelSearchResult notamApiAccessItemModelSearchResult = new NotamApiAccessItemModelSearchResult();
        notamApiAccessItemModelSearchResult.setItems(resultList);
        notamApiAccessItemModelSearchResult.setTotalCount(resultList.size());
        return notamApiAccessItemModelSearchResult;
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

    private String getSortableColumnName(String column){
        String columnValue = sortableFieldsMappingDictionary.get(column);
        return StringUtils.hasText(columnValue) ? columnValue : column;
    }

}
