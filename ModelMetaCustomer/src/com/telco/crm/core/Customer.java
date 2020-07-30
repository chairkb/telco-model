package com.telco.crm.core;

import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.reflect.ReflectField;

public class Customer extends Clasz {
	
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String Salutation;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String FirstName;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String LastName;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String RegisteredMobileNumber;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String CountryCode;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String Uen;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String CustomerType;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String PreferredContactMethod;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String ContactType;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String ContactNumber;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String EmailAddress;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String AddressType;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String AddressLine1;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String AddressLine2;
	@ReflectField(type=FieldType.STRING, size=64, displayPosition=5) public static String Postcode;
}
