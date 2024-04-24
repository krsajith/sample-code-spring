package com.taomish.msservice01.audit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ModelMapperService {

    private final Logger log = LoggerFactory.getLogger(ModelMapperService.class);
    private final ConversionService conversionService = DefaultConversionService.getSharedInstance();
    private final TypeFactory typeFactory = TypeFactory.defaultInstance();
    private final ObjectMapper objectMapper = JsonUtils.getObjectMapper();

    public <D> D map(Object source, Class<D> destinationType) {
        if (source instanceof Optional<?> optional) {
            source = optional.orElse(null);
        }
        if (source == null)
            return null;

        var destinationObject = BeanUtils.instantiateClass(destinationType);
        var bwDestination = new BeanWrapperImpl();
        bwDestination.setConversionService(conversionService);
        bwDestination.setBeanInstance(destinationObject);

        var bwSource = new BeanWrapperImpl();
        bwSource.setConversionService(conversionService);
        bwSource.setBeanInstance(source);


        var destinationFields = BeanUtils.getPropertyDescriptors(destinationType);


        for (var pd : destinationFields) {
            if (bwDestination.isWritableProperty(pd.getName()) && bwSource.isReadableProperty(pd.getName())) {

                try {
                    Object value = bwSource.getPropertyValue(pd.getName());
                    String packageName = pd.getPropertyType().getPackageName();
                    if (!packageName.startsWith("java.lang")
                            && !packageName.startsWith("java.time")
                            && !pd.getPropertyType().equals(Date.class)) {

                        var type = pd.getReadMethod().getGenericReturnType();
                        JavaType jt = typeFactory.constructType(type);
                        value = objectMapper.convertValue(value, jt);
                    }
                    bwDestination.setPropertyValue(pd.getName(), value);
                } catch (Exception e) {
                    log.debug("Error setting field {} of {}, {}", pd, destinationType.getName(), e.getMessage());
                }
            }
        }
        return destinationObject;
    }
}
