package com.taomish.msservice01;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyUtilTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        var customer = new Customer();
        customer.setLastName("Last");
        var propertyTypes = findPropertyTypes(customer);
        System.out.println(propertyTypes);

        System.out.println(PropertyUtils.getProperty(customer,"lastName").toString());
    }

    private static Map<String, String> findPropertyTypes(Customer customer) {
        return Arrays.stream(PropertyUtils.getPropertyDescriptors(customer)).collect(Collectors.toMap(p->p.getName(), p->p.getPropertyType().getSimpleName()));
    }


}
//A.map(p-> p.getPropertyType().getSimpleName())
