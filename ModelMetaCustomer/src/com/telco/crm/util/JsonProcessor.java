package com.telco.crm.util;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldHtml;
import biz.shujutech.db.object.FieldClasz;
import biz.shujutech.db.object.FieldObject;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.Field;
import biz.shujutech.db.relational.FieldDateTime;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.bznes.Addr;
import biz.shujutech.bznes.AddrTypePerson;
import biz.shujutech.bznes.AreaCode;
import biz.shujutech.bznes.Country;
import biz.shujutech.bznes.Ethnicity;
import biz.shujutech.db.relational.FieldBoolean;
import biz.shujutech.db.relational.FieldDate;
import biz.shujutech.bznes.City;
import biz.shujutech.bznes.EmailTypePerson;
import biz.shujutech.bznes.GotIntf;
import biz.shujutech.bznes.Marital;
import biz.shujutech.bznes.MobilePhone;
import biz.shujutech.bznes.Money;
import biz.shujutech.bznes.NetworkDestinationCode;
import biz.shujutech.bznes.Person;
import biz.shujutech.bznes.Spouse;
import biz.shujutech.bznes.State;
import biz.shujutech.bznes.Telephone;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import biz.shujutech.db.object.Lookup;
import biz.shujutech.db.relational.FieldInt;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import org.apache.commons.text.StringEscapeUtils;

public class JsonProcessor {
	public static final int MAX_JSON_LEN = 512;
	public static final HtmlCompressor HtmlMinifier = new HtmlCompressor();

	public static Clasz JsonStr2Clasz(Connection aConn, String aJsonStr) throws Exception {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) parser.parse(aJsonStr);
		Long objectId = jsonObj.get("objectId").getAsLong();
		String claszType = jsonObj.get("clasz").getAsString();
		if (jsonObj.get("clasz") != null) {
			claszType = jsonObj.get("clasz").getAsString();
		}

		Clasz result;
		if (objectId == Clasz.NOT_INITIALIZE_OBJECT_ID) {
			result = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(claszType));
		} else {
			result = ObjectBase.FetchObject(aConn, Class.forName(claszType), objectId); // can tune this for performance?
			if (result == null) {
				result = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(claszType));
			}
		}

		if (jsonObj.has("data")) {
			JsonObject dataObj = jsonObj.get("data").getAsJsonObject();
			JsonObj2ClaszRecursion(aConn, dataObj, result);
		} else {
			throw new Hinderance("The outest structure of the JSON must contain data field: " + claszType + ", oid: " + objectId);
		}
		return(result);
	}

	public static void JsonObj2ClaszRecursion(Connection aConn, JsonObject aJson, Clasz aMasterObj) throws Exception {
		for (Map.Entry<String, JsonElement> entry : aJson.entrySet()) {
			String jsName = entry.getKey();
			try {
	    	JsonElement jsFieldValue = entry.getValue();
				String jsFieldName = DisplayName2CzName(jsName); // convert the json field name to result field name
				if (jsFieldName.toLowerCase().contains("assword")) {
					App.logDebg(JsonProcessor.class, "Converting json field: " + jsName + ", with value: ************");
				} else {
					App.logDebg(JsonProcessor.class, "Converting json field: " + jsName + ", with value: " + jsFieldValue);
				}
	
				if (jsFieldValue.isJsonPrimitive()) {
					if (jsFieldName.compareToIgnoreCase("objectid") == 0) {
						aMasterObj.setObjectId(jsFieldValue.getAsLong()); // force this as modified changes
					} else if (jsFieldName.compareToIgnoreCase("clasz") == 0) {
					} else {
						App.logWarn(JsonProcessor.class, "Ignoring primitive field name: " + jsName + " from the given JSON string");
					}
				} else if (jsFieldValue.isJsonObject()) {
					JsonObject jsObjField = jsFieldValue.getAsJsonObject();
					if (!jsObjField.entrySet().isEmpty()) { // ignore object with no field at all
						if (jsObjField.has("data")) {
							JsonElement dataValue = jsObjField.get("data");
							
							if (!aMasterObj.gotField(jsFieldName)) { // this field is not define during compile time, it's probably an adhoc field
								String fieldType = "";
								if (jsObjField.has("type")) {
									JsonElement dataType = jsObjField.get("type");
									fieldType = dataType.getAsString();
								}

								if (fieldType.equals("boolean")) {
									aMasterObj.createField(jsFieldName, FieldType.BOOLEAN);  
								} else if (fieldType.equals("integer")) {
									aMasterObj.createField(jsFieldName, FieldType.INTEGER);  
								} else if (fieldType.equals("date")) {
									aMasterObj.createField(jsFieldName, FieldType.DATE);  
								} else if (fieldType.equals("datetime")) {
									aMasterObj.createField(jsFieldName, FieldType.DATETIME);  
								} else if (fieldType.equals("html")) {
									aMasterObj.createField(jsFieldName, FieldType.HTML);  
								} else {
									aMasterObj.createField(jsFieldName, FieldType.STRING);  
								}
							}

							if (aMasterObj.getField(jsFieldName).getFieldType() == FieldType.OBJECT) {
								FieldObject fieldObject = (FieldObject) aMasterObj.getField(jsFieldName);
	
								// all member object must be Fetch before saving it, i.e. need its inheritance info etc.
								Long objectId = Clasz.NOT_INITIALIZE_OBJECT_ID;
								if (jsObjField.has("objectId")) {
									objectId = jsObjField.get("objectId").getAsLong();
	
									if (jsObjField.has("delete")) {
										fieldObject.setForDelete(true);
									}

									String claszType = fieldObject.getDeclareType(); // if field object is null, create it
									Clasz fieldClasz;
									if (objectId == Clasz.NOT_INITIALIZE_OBJECT_ID) {
										fieldClasz = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(claszType));
									} else {
										fieldClasz = ObjectBase.FetchObject(aConn, Class.forName(claszType), objectId); // can tune this for performance?
										if (fieldClasz == null) {
											fieldClasz = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(claszType));
										}
									}
									fieldObject.setObj(fieldClasz);
		
									if (fieldObject.isLookup(aConn)) {
										Lookup lookup = (Lookup) fieldObject.getObj();
										Clasz obj = (Clasz) Lookup.GetSelectedLookup(lookup.getLookupList(), dataValue.getAsString());
										fieldObject.setValueObject(obj);
									} else if (fieldObject.getObj() instanceof Money) {
										Money money = (Money) fieldObject.getObj();
										String value = dataValue.getAsString();
										money.setObjectId(objectId);
										money.setValueStr(value);
									} else if (fieldObject.getObj() instanceof GotIntf) {
										GotIntf gotIntf = (GotIntf)	fieldObject.getObj();
										JsonObject jsObj = dataValue.getAsJsonObject();
										for (Map.Entry<String, JsonElement> jsField: jsObj.entrySet()) {
			    						JsonObject jsTypeOrValue = (jsField.getValue()).getAsJsonObject();
											if (jsTypeOrValue.has("lookup")) {
												String lookupStr = (jsTypeOrValue.get("data")).getAsString();
												gotIntf.setType(lookupStr);
											} else {
												JsonElement value = jsTypeOrValue.get("data");
												if (value.isJsonPrimitive()) {
													String valueName = jsField.getKey();
													String jsValueName = DisplayName2CzName(valueName); // convert the json field name to result field name
													Field valueField = fieldObject.getField(jsValueName);
													if (IsModified(aConn, valueField, value) == true) {
														valueField.setValueStr(value.getAsString());
													}
												} else if (value.isJsonObject()) {
													// get the value of the GotIntf, just as how all member object must be Fetch before saving it, i.e. need its inheritance info etc.
													Long objectId1 = Clasz.NOT_INITIALIZE_OBJECT_ID;
													if (jsTypeOrValue.has("objectId")) {
														objectId1 = jsTypeOrValue.get("objectId").getAsLong();
													}
													Clasz valueClasz;
													if (objectId1 == Clasz.NOT_INITIALIZE_OBJECT_ID) {
														valueClasz = (Clasz) gotIntf.createValue(aConn);
													} else {
														valueClasz = (Clasz) gotIntf.getValue(aConn);
													}
													gotIntf.setValue(valueClasz);
		
													//Clasz valueClasz = (Clasz) gotIntf.createValue(aConn);
													JsonObj2ClaszRecursion(aConn, value.getAsJsonObject(), valueClasz);
												} else {
													throw new Hinderance("Expecting field: " + jsFieldName + " to be either FieldObject, Lookup or GotIntf type, but is not!");
												}
											}
										}
	
									} else if (dataValue.isJsonPrimitive()) {
										if (IsModified(aConn, fieldObject, dataValue) == true) {
											fieldObject.setValueStr(aConn, dataValue.getAsString()); // is object but the value in data field is primitive, e.g. Money
										}
									} else if (dataValue.isJsonObject()) {
										JsonObj2ClaszRecursion(aConn, dataValue.getAsJsonObject(), fieldObject.getObj());
									} else {
										throw new Hinderance("Expecting field: " + jsFieldName + " to be either FieldObject, Lookup or GotIntf type, but is not!");
									}
								} else { 
									// it's a OBJECT but with no objectId, meaning is not use, so ignore this field
								}
							} else {
								aMasterObj.getField(jsFieldName).setValueStr(dataValue.getAsString()); // here is where each of primitive field is set
							}
						} else if (jsObjField.has("dataset")) { // no data field, test for dataset field then
								JsonArray dataSet = jsObjField.get("dataset").getAsJsonArray();
								FieldObjectBox fob = (FieldObjectBox) aMasterObj.getField(jsFieldName);
								fob.getObjectMap().clear(); // clear all the fields and fill it according with what's inside the json given, objectbase will by itself determine which member is updated/deleted or inserted
	
								String claszType = fob.getDeclareType();
								JsonAry2Clasz(aConn, claszType, dataSet, fob);
						} else {
							throw new Hinderance("Unsupported/unknown JSON type at: " + jsName + ", is neither data or dataset!");
						}
					}
				} else if (jsFieldValue.isJsonArray()) {
					throw new Hinderance("JSON object must be in the data or dataset field: " + jsFieldValue.getAsString());
				} else {
					throw new Hinderance("Unsupported/Unknown JSON type at: " + jsName);
				}
			} catch(Exception ex) {
				throw new Hinderance(ex, "Fail to convert JSON field: '" + jsName + "', to 'Clasz' field");
			}
		}
	}

	public static boolean IsModified(Connection aConn, Field originalField, JsonElement changedField) throws Exception {
		boolean result = true;
		if (originalField.getValueStr(aConn) == null && changedField.getAsString().isEmpty()) {
			result = false;
		} else if (originalField.getValueStr(aConn).equals(changedField.getAsString()) == true) {
			result = false;
		}
		return(result);
	}

	private static void JsonAry2Clasz(Connection aConn, String strClasz, JsonArray aryObjs, FieldObjectBox result) throws Exception {
		for (int cntr = 0; cntr < aryObjs.size(); cntr++) {
			JsonObject jsonObj = aryObjs.get(cntr).getAsJsonObject();
			Long objectId = jsonObj.get("objectId").getAsLong();
			if (jsonObj.get("clasz") != null) {
				strClasz = jsonObj.get("clasz").getAsString();
			}

			Clasz clasz;
			if (objectId == Clasz.NOT_INITIALIZE_OBJECT_ID) {
				clasz = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(strClasz));
			} else {
				clasz = ObjectBase.FetchObject(aConn, Class.forName(strClasz), objectId); // can tune this for performance?
				if (clasz == null) {
					clasz = (Clasz) ObjectBase.CreateObject(aConn, Class.forName(strClasz));
				}
			}

			if (jsonObj.has("delete")) {
				clasz.setForDelete(true);
			}

			if (jsonObj.has("data")) {
				result.addValueObject(clasz); // place into the fieldobjectbox
				JsonObject dataObj = jsonObj.get("data").getAsJsonObject();
				JsonObj2ClaszRecursion(aConn, dataObj, clasz);
			} else if (jsonObj.has("dataset")) {
				throw new Hinderance("JSON array of element: " + cntr + ", must have data field, not dataset: " + strClasz);
			} else {
				throw new Hinderance("JSON array of element: " + cntr + ", contain invalid JSON structure at: " + strClasz + ", object must had data field");
			}
		}
	}

	public static String SetUiMaster(Field eachField, String result) {
		if (eachField.isUiMaster()) {
			if (result.isEmpty() == false) { result += ", "; }	
			result += App.DoubleQuote("uimaster") + ": true";
		}
		return(result);
	}

	public static String setUpdateable(Field aField, String aStr) {
		if (aField.isUpdateable() == false) {
			if (aStr.isEmpty() == false) { aStr += ", "; }
			aStr += App.DoubleQuote("updateable") + ": " + App.DoubleQuote("false");
		}
		return(aStr);
	}

	public static String setChangeable(Field aField, String aStr) {
		if (aField.isChangeable() == false) {
			if (aStr.isEmpty() == false) { aStr += ", "; }
			aStr += App.DoubleQuote("changeable") + ": " + App.DoubleQuote("false");
		}
		return(aStr);
	}

	@Deprecated
	public static String setData(Field aField, String aStr) throws Exception {
		try {
			if (aStr.isEmpty() == false) { aStr += ", "; }
			aStr += App.DoubleQuote("data") + ": " + App.DoubleQuote(aField.getValueStr());
			return(aStr);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Fail, while setting field data");
		}
	}

	public static String setData(Field aField, String aStr, Connection aConn) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		if (aField instanceof FieldHtml) {
			String escapedHtml = StringEscapeUtils.escapeHtml4(aField.getValueStr(aConn));
			String minifyHtml = HtmlMinifier.compress(escapedHtml);
			aStr += App.DoubleQuote("data") + ": " + App.DoubleQuote(minifyHtml);
		} else {
			aStr += App.DoubleQuote("data") + ": " + App.DoubleQuote(aField.getValueStr(aConn));
		}
		return(aStr);
	}

	public static String setMask(Field aField, String aStr) throws Exception {
		try {
			if (aField.getFieldMask().isEmpty() == false) {
				if (aStr.isEmpty() == false) { aStr += ", "; }
				aStr += App.DoubleQuote("mask") + ": " + App.DoubleQuote(aField.getFieldMask());
			}
			return(aStr);
		} catch(Exception ex) {
			throw new Hinderance(ex, "Fail, while setting field mask");
		}
	}

	public static String setLookup(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr += App.DoubleQuote("lookup") + ": true";
		return(aStr);
	}
	
	public static String setSize(Field aField, String aStr) throws Exception {
		if (aField.getFieldSize() != 0) {
			if (aStr.isEmpty() == false) { aStr += ", "; }
			aStr += App.DoubleQuote("size") + ": " + App.DoubleQuote(Integer.toString(aField.getFieldSize()));
		}
		return(aStr);
	}

	public static String setTypeDatetime(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr += App.DoubleQuote("type") + ": " + App.DoubleQuote("datetime");
		return(aStr);
	}

	public static String setTypeDate(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr += App.DoubleQuote("type") + ": " + App.DoubleQuote("date");
		return(aStr);
	}

	public static String setTypeHtml(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr +=  App.DoubleQuote("type") + ": " + App.DoubleQuote("html");
		return(aStr);
	}

	public static String setTypeBoolean(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr +=  App.DoubleQuote("type") + ": " + App.DoubleQuote("boolean");
		return(aStr);
	}

	public static String setTypeInt(Field aField, String aStr) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr +=  App.DoubleQuote("type") + ": " + App.DoubleQuote("integer");
		return(aStr);
	}

	public static String setClasz(Field aField, String aStr, String clsCmn) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr += App.DoubleQuote("clasz") + ": " + App.DoubleQuote(clsCmn);
		return(aStr);
	}

	public static String setObjectId(Field aField, String aStr, Clasz obj) throws Exception {
		if (aStr.isEmpty() == false) { aStr += ", "; }
		aStr += App.DoubleQuote("objectId") + ": " + obj.getObjectId();
		return(aStr);
	}

	public static void ReplaceStrBuffer(StringBuffer builder, String from, String to) {
		int index = builder.indexOf(from);
		builder.replace(index, index + from.length(), to);
	}


	public static String GetPlaceHolderStr(Integer aCntr) {
		return("marker" + aCntr + "marker");
	}

	/**
	 * Fields to be ignore
	 * 
	 * @param aFieldName
	 * @return 
	 */
	public static boolean isDataField(String aFieldName) {
		boolean result = true;
		if (aFieldName.toLowerCase().equals("clasz")) {
			result = false;
		}
		return(result);
	}

	public static String AsJsonStrObj(Connection aConn, Clasz aDataObj) throws Exception {
		CopyOnWriteArrayList<Clasz> avoidRecursive = new CopyOnWriteArrayList<>();
		String jsonStr = "{ ";
		jsonStr += App.DoubleQuote("objectId") + ": " + aDataObj.getObjectId() + ", "; 
		jsonStr += App.DoubleQuote("clasz") + ": " + App.DoubleQuote(aDataObj.getClass().getName()) + ", "; 
		jsonStr += App.DoubleQuote("data") + ": " + "{ ";
		jsonStr += JsonProcessor.AsJsonRecursion(aConn, aDataObj, avoidRecursive); 
		jsonStr += " }";
		jsonStr += " }";
		return(jsonStr);
	}

	private static String AsJsonStrAry(String jsonString) throws Exception {
		return(AsJsonStrAry(jsonString, null, null, ""));
	}

	private static String AsJsonStrAry(String jsonString, String aChildName) throws Exception {
		return(AsJsonStrAry(jsonString, null, aChildName, ""));
	}

	private static String AsJsonStrAry(String jsonString, Clasz aParent, String aChildName, String aParentName) throws Exception {
		String status = App.DoubleQuote("status") + ": " + App.DoubleQuote("ok")
		+ ", " + App.DoubleQuote("msg") + ": " + App.DoubleQuote("Records successfully retrieve"); 

		String childName = "";
		if (aChildName != null) {
			childName = App.DoubleQuote("childName") + ": " + App.DoubleQuote(aChildName);
		} else {
			if (aParent != null) {
				childName = aParent.getClaszName();
			}
		}

		String aryMeta;
		if (aParent == null) {
			aryMeta = "{" 
							+ childName
							+ "}";
		} else {
			if (childName.isEmpty() == false) childName += ", ";
			aryMeta = "{" 
							+ childName
							+ App.DoubleQuote("parentClasz") + ": " 
							+ App.DoubleQuote(aParent.getClass().getName()) 
							+ ", " 
							+ App.DoubleQuote("parentOid") + ": " 
							+ App.DoubleQuote(Long.toString(aParent.getObjectId())) 
							+ ", " 
							+ App.DoubleQuote("parentName") + ": " 
							+ App.DoubleQuote(aParentName) 
							+ "}";
		}
		return("{" + status + ", " + App.DoubleQuote("meta") + ": " + aryMeta + ", " + App.DoubleQuote("dataset") + ": " + "[" + jsonString + " ]}");
	}

	public static String Object2Json(Connection aConn, Clasz aDataObj) throws Exception {
		return(Object2Json(aConn, aDataObj, null));
	}

	public static String Object2Json(Connection aConn, Clasz aDataObj, String aChildName) throws Exception {
		String strObj = AsJsonStrObj(aConn, aDataObj);
		String result = AsJsonStrAry(strObj, aChildName);
		return(result);
	}

	public static String DisplayName2CzName(String aDisplayName) {
		aDisplayName = aDisplayName.replace(" ", "_");
		aDisplayName = aDisplayName.toLowerCase();
		return(aDisplayName);
	}

	public static String SetCountryState(String strObj, Field eachField, String typeName) throws Exception {
		strObj += "" + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr());
		strObj += ", " + App.DoubleQuote("option") + ": {";
		int cntrComa = 0;
		for(Lookup eachObj: Country.getCountryList()) {
			Country country = (Country) eachObj;
			if (country.getDescr().trim().isEmpty() == false) {
				if (cntrComa != 0) {
					strObj += ", ";
				}
				cntrComa++;

				String strState = "";
				for (Clasz eachState: country.getState().getObjectMap().values()) {
					State state = (State) eachState;
					if (strState.isEmpty() == false) {
						strState += ", ";
					}
					strState += App.DoubleQuote(state.getDescr());
				}
				strObj += App.DoubleQuote(country.getDescr()) + ": [" + strState + "]";
			}
		}
		strObj += "}";
		strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote(typeName) + ", ";
		return(strObj);
	}

	public static String SetStateCity(String strObj, Field eachField, String typeName) throws Exception {
		strObj += "" + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr());
		strObj += ", " + App.DoubleQuote("option") + ": {";
		int cntrComa = 0;
		for(Lookup eachObj: State.GetStateList()) {
			State state = (State) eachObj;
			if (state.getDescr().trim().isEmpty() == false) {
				if (cntrComa != 0) {
					strObj += ", ";
				}
				cntrComa++;

				String strCity = "";
				for (Clasz eachCity: state.getCity().getObjectMap().values()) {
					City city = (City) eachCity;
					if (strCity.isEmpty() == false) {
						strCity += ", ";
					}
					strCity += App.DoubleQuote(city.getDescr());
				}
				strObj += App.DoubleQuote(state.getDescr()) + ": [" + strCity + "]";
			}
		}
		strObj += "}";
		strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote(typeName) + ", ";
		return(strObj);
	}

	public static String SetCountry(String strObj, Field eachField, String typeName) throws Exception {
		strObj += "" + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr());
		strObj += ", " + App.DoubleQuote("countryaddr") + ": {";
		int cntrComa = 0;
		for(Lookup eachObj: Country.getCountryList()) {
			Country country = (Country) eachObj;
			if (country.getDescr().trim().isEmpty() == false) {
				if (cntrComa != 0) {
					strObj += ", ";
				}
				cntrComa++;

				String strState = "";
				for (Clasz eachState: country.getState().getObjectMap().values()) {
					State state = (State) eachState;
					if (strState.isEmpty() == false) {
						strState += ", ";
					}

					String strCity = "";
					for (Clasz eachCity: state.getCity().getObjectMap().values()) {
						City city = (City) eachCity;
						if (strCity.isEmpty() == false) {
							strCity += ", ";
						}
						strCity += App.DoubleQuote(city.getDescr());
					}
					strState += "{" + App.DoubleQuote(state.getDescr()) + ": [" + strCity + "]" + "}";
				}
				strObj += App.DoubleQuote(country.getDescr()) + ": [" + strState + "]";
			}
		}
		strObj += "}";
		strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote(typeName) + ", ";
		return(strObj);
	}

	public static String SetComboboxFilter(String strObj, Field eachField, String masterName, CopyOnWriteArrayList<Lookup> masterList, String typeName) throws Exception {
		return(SetComboboxFilter(strObj, eachField, masterName, masterList, "", typeName));
	}

	public static String SetComboboxFilter(String strObj, Field eachField, String masterName, CopyOnWriteArrayList<Lookup> masterList, String childName, String typeName) throws Exception {
		strObj += "" + App.DoubleQuote("option") + ":" + " {";
		int cntrComa = 0;
		for(Lookup eachObj: masterList) {
			Lookup childLookup = (Lookup) eachObj;
			String txt = childLookup.getDescr();
			if (txt.isEmpty() == false) {
				if (cntrComa != 0) {
					strObj += ", ";
				}
				cntrComa++;
				strObj += App.DoubleQuote(txt);

				String strAry = "";
				if (childName != null && childName.equals("") == false) {
					for (Clasz eachChild: ((Clasz) childLookup).getFieldObjectBox(childName).getObjectMap().values()) {
						Lookup child = (Lookup) eachChild;
						if (strAry.isEmpty() == false) {
							strAry += ", ";
						}
						strAry += App.DoubleQuote(child.getDescr());
					}
				}
				strObj += ": [" + strAry + "]";
			}
		}
		strObj += "}";
		strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote(typeName) + ", ";
		return(strObj);
	}

	public static String AsJsonRecursion(Connection aConn, Clasz aDataObj, List aAvoidRecursion) throws Exception {
		String result = "";
		CopyOnWriteArrayList<Thread> threadPool = new CopyOnWriteArrayList<>();
		ConcurrentHashMap<String, String> threadResultFob = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, String> threadResultField = new ConcurrentHashMap<>();
		aDataObj.displayAll(true);
		List<Field> fieldList = aDataObj.getFieldListByDisplayPosition();

		for (Field eachField : fieldList) {
			if (eachField.forDisplay() == false) {
				continue;
			}

			if (result.isEmpty() == false) {
				result += ", ";
			}

			result += App.DoubleQuote(eachField.getDisplayName()) + ":";	
			boolean isObjLookup = false;
			if (eachField instanceof FieldObject) {
				if (((FieldObject) eachField).isLookup(aConn)) {
					isObjLookup = true;
				}
			}
			if (!(eachField instanceof FieldObject || eachField instanceof FieldObjectBox) || isObjLookup) { // is not object type, i.e. process atomic type
				if (isObjLookup) {
					result += " {";
					Lookup isLookup = (Lookup) eachField.getValueObj(aConn);
					if (isLookup != null) {
						if (isLookup instanceof Country) {
							result = SetComboboxFilter(result, eachField, "country", Country.LookupList, "country");
						} else if (isLookup instanceof State) {
							result = SetCountryState(result, eachField, "state");
						} else if (isLookup instanceof City) {
							result = SetStateCity(result, eachField, "city");
						} else {
							result += "" + App.DoubleQuote("option") + ":" + " {";
							int cntrComa = 0;
							for(Lookup eachLookup: isLookup.getLookupList()) {
								String txt = (eachLookup).getDescr();
								if (txt.isEmpty() == false) {
									if (cntrComa != 0) {
										result += ",";
									}
									cntrComa++;
									result += App.DoubleQuote(txt) + ": []";
								}
							}
							result += " " + "},";
						}
						String fieldArea = "";
						if (isLookup instanceof Clasz) {
							fieldArea = setObjectId(eachField, fieldArea, (Clasz) isLookup);
						}
						fieldArea = setData(eachField, fieldArea, aConn);
						fieldArea = setUpdateable(eachField, fieldArea);
						fieldArea = setChangeable(eachField, fieldArea);
						fieldArea = setLookup(eachField, fieldArea);
						fieldArea = setSize(eachField, fieldArea);
						result += fieldArea;
					}
					result += " }";
				} else if (eachField instanceof FieldDateTime) {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setTypeDatetime(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				} else if (eachField instanceof FieldDate) {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setTypeDate(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				} else if (eachField instanceof FieldHtml) {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setTypeHtml(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				} else if (eachField instanceof FieldBoolean) {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setTypeBoolean(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				} else if (eachField instanceof FieldInt) {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setTypeInt(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				} else {
					result += " {";
					String fieldArea = "";
					fieldArea = setData(eachField, fieldArea, aConn);
					fieldArea = setMask(eachField, fieldArea);
					fieldArea = setUpdateable(eachField, fieldArea);
					fieldArea = setChangeable(eachField, fieldArea);
					fieldArea = setSize(eachField, fieldArea);
					result += fieldArea;
					result += " }";
				}
			} else if (eachField instanceof FieldObjectBox) {
				FieldObjectBox fob = (FieldObjectBox) eachField;
				String clsCmn = ((FieldObjectBox) eachField).getDeclareType().trim();
				result += " {";
				String fieldArea = "";
				fieldArea = SetUiMaster(eachField, fieldArea);
				fieldArea = setUpdateable(eachField, fieldArea);
				fieldArea = setChangeable(eachField, fieldArea);
				fieldArea = setClasz(eachField, fieldArea, clsCmn);
				if (result.isEmpty() == false) {
					result += " ";
				}
				result += fieldArea + ", ";
				result += App.DoubleQuote("dataset") + ": " +  "[";
				if (fob.getTotalMember() == 0) {
					// do nothing
				} else {
					int cntrFob = 0;
					boolean isFirst = true;
					for(Clasz obj : fob.getObjectMap().values()) {
						if (obj != null) {
							if (aAvoidRecursion.contains(obj) == false) {
								aAvoidRecursion.add(obj);
								if (isFirst) {
									result += " {";
									isFirst = false;
								} else {
									result += ", {";
								}
								String clsPolymorphic = obj.getClass().getName().trim();
								if (clsCmn.equals(clsPolymorphic) == false) {
									result += App.DoubleQuote("clasz") + ": " + App.DoubleQuote(clsPolymorphic);
								} else {
									result += App.DoubleQuote("clasz") + ": " + App.DoubleQuote(clsCmn);
								} 
								result += ", " + App.DoubleQuote("objectId") + ": " + obj.getObjectId();
								if (obj.getForDelete()) {
									result += ", " + App.DoubleQuote("delete") + ": " + obj.getForDelete();
								}
								result += ", " + App.DoubleQuote("data") + ": " + "{";
								String strKey = aDataObj.getClaszName() + "." + fob.getFieldName() + "." + obj.getObjectId() + "." + cntrFob++;
								//result += JsonProcessor.AsJsonRecursion(aDb, aConn, obj, aAvoidRecursion);
								JsonProcessor.JsonRecursionSpawnThread(threadPool, threadResultFob, strKey, aConn, obj, aAvoidRecursion);
								result += "+++" + strKey + "===";
								result += "}";
								result += " }";
							}
						}
					}
				}
				result += " ]";
				result += " }";
			} else if (eachField instanceof FieldObject) {
				Clasz obj = null;
				if (((FieldObject) eachField).getObj() != null) {
					obj = (Clasz) eachField.getValueObj(aConn); // only get the objet if its already populated
				}
				result += " {";
				String fieldArea = "";
				fieldArea = setUpdateable(eachField, fieldArea);
				fieldArea = setChangeable(eachField, fieldArea);
				fieldArea = SetUiMaster(eachField, fieldArea);
				if (obj == null) {
					fieldArea = setClasz(eachField, fieldArea, ((FieldClasz) eachField).getDeclareType());
				} else {
					fieldArea = setClasz(eachField, fieldArea, obj.getClass().getName());
				}
				String strObj = "";
				if (obj != null) {
					fieldArea = setObjectId(eachField, fieldArea, obj);
					if (obj.getClaszName().equals("clasz") == false) {
						if (eachField.getValueObj(aConn) instanceof Money) {
							strObj += ", " + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr(aConn));
							strObj += ", " + App.DoubleQuote("currencies") + ": {";
							int cntrComa = 0;
							for(Lookup eachObj: Country.getCountryList()) {
								String txt = ((Country) eachObj).getCurrencyCode();
								if (txt.isEmpty() == false) {
									if (cntrComa != 0) {
										strObj += ", ";
									}
									cntrComa++;
									strObj += App.DoubleQuote(txt) + ": " + App.DoubleQuote(txt);
								}
							}
							strObj += " }";
							strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote("money") + "";
						} else if (eachField.getValueObj(aConn) instanceof MobilePhone) {
							strObj += ", " + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr(aConn));
							strObj += ", " + App.DoubleQuote("countrycode") + ": {";
							int cntrComa = 0;
							for(Lookup eachObj: Country.getCountryList()) {
								Country country = (Country) eachObj;
								String txt = country.getDescr() + " " + country.getCountryCallingCode();
								if (txt.isEmpty() == false) {
									if (cntrComa != 0) {
										strObj += ", ";
									}
									cntrComa++;
		
									String strAry = "";
									for (Clasz eachNdc : country.getNdc().getObjectMap().values()) {
										NetworkDestinationCode ndc = (NetworkDestinationCode) eachNdc;
										if (strAry.isEmpty() == false) {
											strAry += ", ";
										}
										strAry += App.DoubleQuote(ndc.getNdc());
									}
									strObj += App.DoubleQuote(txt) + ": [" + strAry + "]";
								}
							}
							strObj += " }";
							strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote("mobilephone");
						} else if (eachField.getValueObj(aConn) instanceof Telephone) {
							strObj += ", " + App.DoubleQuote("data") + ": " + App.DoubleQuote(eachField.getValueStr());
							strObj += ", " + App.DoubleQuote("countrycode") + ": {";
							int cntrComa = 0;
							for(Lookup eachObj: Country.getCountryList()) {
								Country country = (Country) eachObj;
								String txt = country.getDescr() + " " + country.getCountryCallingCode();
								if (txt.isEmpty() == false) {
									if (cntrComa != 0) {
										strObj += ", ";
									}
									cntrComa++;
		
									String strAry = "";
									for (Clasz eachArea : country.getAreaCode().getObjectMap().values()) {
										AreaCode areaCode = (AreaCode) eachArea;
										if (strAry.isEmpty() == false) {
											strAry += ", ";
										}
										strAry += App.DoubleQuote(areaCode.getAreaCode());
									}
									strObj += App.DoubleQuote(txt) + ": [" + strAry + "]";
								}
							}
							strObj += " }";
							strObj += ", " + App.DoubleQuote("type") + ": " + App.DoubleQuote("telephone");
						} else {
							if (aAvoidRecursion.contains(obj) == false) {
								//App.logDebg(JsonProcessor.class, "Recursing object: " + obj.getClaszName());
								aAvoidRecursion.add(obj);
								String strKey = aDataObj.getClaszName() + "." + eachField.getFieldName();
								JsonProcessor.JsonRecursionSpawnThread(threadPool, threadResultField, strKey, aConn, obj, aAvoidRecursion);
								strObj += "+++" + strKey + "===";
							}
						}
					} else {
						//App.logDebg(JsonProcessor.class, "NOT PROCESS: " + eachField.getDisplayName() + ", result: " + ((FieldObject) eachField).getDeclareType());
						String strNot = "";
						strNot = setClasz(eachField, strNot, ((FieldObject) eachField).getDeclareType());
						strNot = setUpdateable(eachField, strNot);
						strNot = setChangeable(eachField, strNot);
						strObj += strNot;
					}
				} else { // object type field with no object
					strObj += ", " + App.DoubleQuote("data") + ": {" + strObj + "}";
				}
				result += fieldArea;
				result += strObj;
				result += " }";
			} else {
				//App.logDebg(JsonProcessor.class, "NOT PROCESS: " + eachField.getDisplayName());
			}
			//App.logDebg(JsonProcessor.class, "Performance to convert JSON field: " + eachField.getFieldName() + ", duration: "  + App.GetLapseTime(startField) + " sec");
		}

		//App.logDebg(JsonProcessor.class, "Waiting for all JsonRecursionSpawnThread to complete, total thread: " + threadPool.size() + ", total wait: " + totalWait);
		for(Thread eachThread : threadPool) {
			eachThread.join(); // for each spawn thread, call join to wait for them to complete
		}
		//App.logDebg(JsonProcessor.class, "Completed submitted thread to JsonRecursionSpawnThread");

		for (Map.Entry<String, String> entry : threadResultFob.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String replaceTarget = "+++" + key + "===";
			//App.logDebg(JsonProcessor.class, "FIELD OBJECT BOX REPLACE: " + replaceTarget + ", VALUE: " + value);
			result = result.replace(replaceTarget, value);
		}

		for (Map.Entry<String, String> entry : threadResultField.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value.isEmpty() == false) {
				String replaceTarget = "+++" + key + "===";
				String strObj = ", " + App.DoubleQuote("data") + ": {" + value + "}";
				result = result.replace(replaceTarget, strObj);
			}
		}

		// replace the placeholder with the reusult of the threaded result
		//App.logDebg(JsonProcessor.class, "Performance for JSON field: " + aDataObj.getClaszName() + ", duration: "  + App.GetLapseTime(startAt) + " sec");
		return(result);
	}

	// this is called by GetJsonListStr and it recurse, therefore
	// we will not use dedicated db connection from the connection pool,
	// instead, we reuse the pass in connection
	public static void JsonRecursionSpawnThread(CopyOnWriteArrayList<Thread> threadPool, Map aResult, String aKey, Connection aConn, Clasz aDataObj, List aAvoidRecursion) throws Exception {
		Integer[] cntrThreadPassAsRef = {0};
		if (App.getMaxThread() == 1) { // no threading
			cntrThreadPassAsRef[0]++;
			String jsonStr = JsonProcessor.AsJsonRecursion(aConn, aDataObj, aAvoidRecursion);
			aResult.put(aKey, jsonStr);
		} else {
			int cntrAttempt = 0;
			int maxAttempt = App.MAX_GET_CONNECTION_ATTEMPT;
			Connection conn = null;
			while(true) {
				try {
					cntrAttempt++;
					//conn = aConn.getBaseDb().getConnPool().getConnection(); // here will throw exception from connection pool, if too many attempts
					conn = aConn;
					if (cntrThreadPassAsRef[0] >= App.getMaxThread()) {
						//aConn.getBaseDb().getConnPool().freeConnection(conn); 
						App.ShowThreadingStatus(JsonProcessor.class, "JsonRecursionSpawnThread", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
						Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // if max thread reached, wait again...
					} else { 
						cntrThreadPassAsRef[0]++;
						Thread theThread = new JsonRecursionThread(cntrThreadPassAsRef, aResult, aKey, conn, aDataObj, aAvoidRecursion);
						threadPool.add(theThread);
						break;
					}
				} catch(Exception ex) {
					App.ShowThreadingStatus(JsonProcessor.class, "JsonRecursionSpawnThread", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
					if (cntrAttempt >= maxAttempt) {
						throw new Hinderance(ex, "[JsonRecursionSpawnThread] Give up threading due to insufficent db connection");
					} else {
						Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // wait for other db conn to free up
					}
				} 
			}
		}
	}

	static class JsonRecursionThread extends Thread {
		Integer[] cntrThread;
		Map result;
		String strKey;
		ObjectBase dbase;
		Connection conn;
		Clasz dataObj;
		List avoidRecursion;

		public JsonRecursionThread(Integer[] aCntrThread, Map aResult, String aKey, Connection aConn, Clasz aDataObj, List aAvoidRecursion) throws Exception {
			this.cntrThread = aCntrThread;
			this.result = aResult;
			this.strKey = aKey;
			this.conn = aConn;
			this.dataObj = aDataObj;
			this.avoidRecursion = aAvoidRecursion;
			start();
		}

		@Override
		public void run() {
			try {
				String jsonStr = JsonProcessor.AsJsonRecursion(this.conn, this.dataObj, this.avoidRecursion);
				this.result.put(this.strKey, jsonStr);
			} catch (Exception ex) {
				App.logEror(JsonProcessor.class, ex, "Fail thread: " + this.cntrThread[0] + ", when converting field to JSON: ");
			} finally {
				// if (App.getMaxThread() != 1) this.conn.getBaseDb().getConnPool().freeConnection(this.conn); // this is recursion, we reuse pass in connection instead of getting a dedicated conn from the pool
				decreThreadCount(this.cntrThread);
			}
		}

		public synchronized void decreThreadCount(Integer[] aCntrThread) {
			aCntrThread[0]--;
		}
	}

	public static Person createPerson(Connection aConn, ObjectBase objectDb) throws Exception {
			// create a new person
			App.logDebg(JsonProcessor.class, "Creating a person for this company");
			Person person = (Person) objectDb.createObject(Person.class);
			person.setName("Edward Jason");
			person.setBirthDate(new DateTime());
			person.setGender(biz.shujutech.bznes.Gender.Male);
			person.setNationality(Country.Malaysia);
			//person.setPassportNo("S1845840");
			person.setMaritalStatus(Marital.Married);
			person.setEthnic(Ethnicity.Caucasian);
			person.setReligion(biz.shujutech.bznes.Religion.Christian);
			//person.addLeavePolicy(objectDb, new DateTime(), LeavePolicy.Malaysia);

			// create person home address
			App.logDebg(JsonProcessor.class, "Creating the person's address");
			Addr homeAddr = person.addAddr(aConn, AddrTypePerson.Home);
			homeAddr.setType(AddrTypePerson.Home);
			homeAddr.setAddr1("SD-35-6, Jalan Rungkup");
			homeAddr.setAddr2("Taman Uu Lian");
			homeAddr.setPostalCode("51200");
			homeAddr.setState(Country.Malaysia.getState(aConn, "Wilayah Perseketuan"));
			homeAddr.setCity("Kuala Lumpur");
			homeAddr.setCountry(Country.Malaysia);

			// create email
			App.logDebg(JsonProcessor.class, "Creating the person's email");
			person.addEmail(aConn, EmailTypePerson.Personal, "edward.jason@oodb.com");
			App.logDebg(JsonProcessor.class, "Created object with email: " + person.getEmail(EmailTypePerson.Personal));

			// create a person spouse
			App.logDebg(JsonProcessor.class, "Creating the person's spouse object");
			Spouse spouse = person.createSpouse("Pauline Jason");
			spouse.setName("Pauline Jason");
			spouse.setBirthDate(new DateTime());
			spouse.setGender(biz.shujutech.bznes.Gender.Female);
			spouse.setNationality(Country.Malaysia);
			//spouse.setIdentityCardNo("A1845940");
			//spouse.setPassportNo("S1835840");
			//spouse.setMaritalStatus(Marital.Married); // if spouse, it means married, so this field was removed?
			spouse.setEthnic(Ethnicity.Caucasian);
			spouse.setReligion(biz.shujutech.bznes.Religion.Christian);

			// create a son of the person
			App.logDebg(JsonProcessor.class, "Creating the person's son object into the person list of children");
			Person eldestSon = person.addChildren(aConn, "Jeremy Jason");
			eldestSon.setBirthDate((new DateTime()));
			eldestSon.setGender(biz.shujutech.bznes.Gender.Male);
			eldestSon.setNationality(Country.Malaysia);
			//eldestSon.setIdentityCardNo("M1845940");
			//eldestSon.setPassportNo("X1835840");
			eldestSon.setMaritalStatus(Marital.Single);
			eldestSon.setEthnic(Ethnicity.Caucasian);
			eldestSon.setReligion(biz.shujutech.bznes.Religion.Christian);

			// create a daughther of the person
			App.logDebg(JsonProcessor.class, "Creating the person's daughther object into the person list of children");
			Person eldestDaughther = person.addChildren(aConn, "Jenny Jason");
			eldestDaughther.setName("Jenny Jason");
			//eldestDaughther.setBirthDate(getDate("01-JAN-1980 01:01:01"));
			eldestDaughther.setBirthDate(new DateTime());
			eldestDaughther.setGender(biz.shujutech.bznes.Gender.Female);
			eldestDaughther.setNationality(Country.Malaysia);
			//eldestDaughther.setNationalId("F1845940");
			//eldestDaughther.setPassportNo("J1335840");
			eldestDaughther.setMaritalStatus(Marital.Single);
			eldestDaughther.setEthnic(Ethnicity.Caucasian);
			eldestDaughther.setReligion(biz.shujutech.bznes.Religion.Christian);

			ObjectBase.PersistCommit(aConn, person);
			return(person);
	}

	//
	// Converting Clasz object to JSON with standard reply format to frontend
	//
	public static String GetJsonListStr(Connection aConn, Clasz aOneClasz) throws Exception {
		List<Clasz> claszList = new CopyOnWriteArrayList<>();
		claszList.add(aOneClasz);
		return(GetJsonListStr(aConn, claszList, ""));
	}

	public static String GetJsonListStr(Connection aConn, List<Clasz> aObjList) throws Exception {
		return(GetJsonListStr(aConn, aObjList, ""));
	}

	public static String GetJsonListStr(Connection aConn, List<Clasz> aObjList, String aChildName) throws Exception {
		List<Thread> threadPool = new CopyOnWriteArrayList<>();
		List<String> threadResult = new CopyOnWriteArrayList<>();
		Integer[] cntrThreadPassAsRef = {0};
		Integer[] cntrSeq = {-1};
		String jsonArray = "";
		for(Clasz eachObj : aObjList) {
			if (jsonArray.isEmpty() == false) {
				jsonArray += ", ";
			}
			GetJsonListStrSpawner(aConn, cntrThreadPassAsRef, threadPool, threadResult, eachObj, cntrSeq);
		}
		for(Thread eachThread : threadPool) {
			eachThread.join(); // for each spawn thread, call join to wait for them to complete
		}

		for(String threadJsonStr : threadResult) {
			if (threadJsonStr.isEmpty() == false) {
				if (jsonArray.isEmpty() == false) {
					jsonArray += ", ";
				}
				jsonArray += threadJsonStr;
			} else {
				App.logWarn(JsonProcessor.class, "Empty result from threaded GetJsonListStr");
			}
		}
		String result = JsonProcessor.AsJsonStrAry(jsonArray, aChildName);
		return(result);
	}

	//
	//
	// Converting Clasz object to JSON with standard reply format to frontend
	//
	//

	public static String GetJsonListStr(Connection aConn, FieldObjectBox objList, Clasz aParent, String aChildName) throws Exception {
		return(GetJsonListStr(aConn, objList, aParent, aChildName, ""));
	}

	public static String GetJsonListStr(Connection aConn, FieldObjectBox objList, Clasz aParent, String aChildName, String aParentName) throws Exception {
		CopyOnWriteArrayList<Thread> threadPool = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<String> threadResult = new CopyOnWriteArrayList<>();
		Integer[] cntrThreadPassAsRef = {0};
		Integer[] cntrSeq = {-1};
		String jsonArray = "";
		objList.resetIterator();
		while (objList.hasNext(aConn)) {
			Clasz eachObj = objList.getNext();
			if (jsonArray.isEmpty() == false) {
				jsonArray += ", ";
			}
			GetJsonListStrSpawner(aConn, cntrThreadPassAsRef, threadPool, threadResult, eachObj, cntrSeq);
		}

		for(Thread eachThread : threadPool) {
			eachThread.join(); // for each spawn thread, call join to wait for them to complete
		}

		for(int cntr = 0; cntr < threadResult.size(); cntr++) {
			if (threadResult.get(cntr).isEmpty() == false) {
				if (jsonArray.isEmpty() == false) {
					jsonArray += ", ";
				}
				jsonArray += threadResult.get(cntr);
			} else {
				App.logWarn(JsonProcessor.class, "Empty result from threaded GetJsonListStr at array: " + cntr);
			}
		}
		String result = JsonProcessor.AsJsonStrAry(jsonArray, aParent, aChildName, aParentName);

		return(result);
	}

	public static void GetJsonListStrSpawner(Connection aConn, Integer[] cntrThreadPassAsRef, List<Thread> threadPool, List<String> threadResult, Clasz aObj, Integer[] cntrSeq) throws Exception {
		if (App.getMaxThread() == 1) { // no threading
			cntrThreadPassAsRef[0]++;
			threadResult.add("");
			cntrSeq[0]++;
			(new GetJsonListStrThread(cntrThreadPassAsRef, aConn, aObj, threadResult, cntrSeq)).join();
		} else {
			int cntrAttempt = 0;
			int maxAttempt = App.MAX_GET_CONNECTION_ATTEMPT;
			Connection conn = null;
			while(true) {
				try {
					cntrAttempt++;
					conn = aConn.getBaseDb().getConnPool().getConnection(); // here will throw exception from connection pool, if too many attempts
					if (cntrThreadPassAsRef[0] >= App.getMaxThread()) {
						aConn.getBaseDb().getConnPool().freeConnection(conn); 
						App.ShowThreadingStatus(JsonProcessor.class, "GetJsonListStrThread", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
						Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // if no db connection, wait x second and continue the loop
					} else { // if there is free db connection
						cntrThreadPassAsRef[0]++;
						threadResult.add("");
						cntrSeq[0]++;
						threadPool.add(new GetJsonListStrThread(cntrThreadPassAsRef, conn, aObj, threadResult, cntrSeq));
						break;
					}
				} catch(Exception ex) {
					App.ShowThreadingStatus(JsonProcessor.class, "GetJsonListStrThread", cntrThreadPassAsRef[0], App.getMaxThread(), cntrAttempt, maxAttempt);
					if (cntrAttempt >= maxAttempt) {
						throw new Hinderance(ex, "[GetJsonListStrThread] Give up threading due to insufficent db connection");
					} else {
						Thread.sleep(App.MAX_THREAD_OR_CONN_WAIT); // wait for other db conn to free up
					}
				} 
			}
		}
	}

	static class GetJsonListStrThread extends Thread {
		Integer[] cntrThread;
		Connection conn;
		Clasz obj2Convert;
		Integer cntrSeq;
		List<String> result;

		public GetJsonListStrThread(Integer[] aCntrThread, Connection aConn, Clasz aObj, List<String> aResult, Integer[] aCntrSeq) throws Exception {
			this.cntrThread = aCntrThread;
			this.conn = aConn;
			this.obj2Convert = aObj;
			this.result = aResult;
			this.cntrSeq = aCntrSeq[0];
			start();
		}

		@Override
		public void run() {
			try {
				String jsonStr = JsonProcessor.AsJsonStrObj(this.conn, this.obj2Convert);
				this.result.set(this.cntrSeq, jsonStr);  // now put the result into array
			} catch (Exception ex) {
				App.logEror(JsonProcessor.class, ex, "Fail thread: " + this.cntrThread[0] + ", in GetJsonListStr for object: " + this.obj2Convert.getClass().getSimpleName());
			} finally {
				if (App.getMaxThread() != 1) this.conn.getBaseDb().getConnPool().freeConnection(this.conn);
				decreThreadCount(this.cntrThread);
			}
		}

		public synchronized void decreThreadCount(Integer[] aCntrThread) {
			aCntrThread[0]--;
		}
	}

}