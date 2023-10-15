package com.example.sevice.batis.service.intr;

import com.example.sevice.batis.domain.Address;
import com.example.sevice.batis.domain.Person;
import org.apache.ibatis.annotations.*;

public interface AddressMapper {
    @Insert("Insert into address (streetAddress,personId) values(#{streetAddress},#{personId})")
    @Options(useGeneratedKeys = true)
    Integer saveAddress(Address address);

    @Select("SELECT addressId, streetAddress FROM Address WHERE addressId = #{addressId}")
    @Results(value = {@Result(property = "addressId", column = "addressId"),
            @Result(property = "streetAddress", column = "streetAddress"),
            @Result(property = "person", column = "personId", javaType = Person.class, one = @One(select = "getPerson"))})
    Address getAddresses(Integer addressId);

    @Select("SELECT personId FROM address WHERE addressId = #{addressId})")
    Person getPerson(Integer addressId);
}