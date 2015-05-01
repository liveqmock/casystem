package ca.core.logic;

import java.beans.IntrospectionException; 
import java.beans.Introspector; 
import java.beans.PropertyDescriptor; 
import java.math.BigDecimal; 
import java.math.BigInteger; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 

public class ToJsonUtil {  
   
  public static String objectTojson(Object obj) {
    StringBuilder json = new StringBuilder();  
    if (obj == null) {  
      json.append("\"\"");  
    } else if (obj instanceof String) {
    	json.append("\"").append(stringTojson(obj.toString())).append("\"");
    } else if (obj instanceof Integer || 
         obj instanceof Float  || 
         obj instanceof Boolean || 
         obj instanceof Short || 
         obj instanceof Double || 
         obj instanceof Long || 
         obj instanceof BigDecimal || 
         obj instanceof BigInteger || 
         obj instanceof Byte) {
    	//json.append("\"").append(stringTojson(obj.toString())).append("\"");
    	json.append(stringTojson(obj.toString())); 
    } else if (obj instanceof Object[]) {  
      json.append(arrayTojson((Object[]) obj));  
    } else if (obj instanceof List<?>) {  
      json.append(listTojson((List<?>) obj));  
    } else if (obj instanceof Map<?, ?>) {  
      json.append(mapTojson((Map<?, ?>) obj));  
    } else if (obj instanceof Set<?>) {  
      json.append(setTojson((Set<?>) obj));  
    } else {  
      json.append(beanTojson(obj));  
    }  
    return json.toString();  
  }  

   
  public static String beanTojson(Object bean) {  
    StringBuilder json = new StringBuilder();  
    json.append("{");  
    PropertyDescriptor[] props = null;  
    try {  
      props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();  
    } catch (IntrospectionException e) {}  
    if (props != null) {  
      for (int i = 0; i < props.length; i++) {  
        try {  
          String name = objectTojson(props[i].getName());
          // 特殊要求： “data” -> "Data"
          StringBuilder sb = new StringBuilder(); 
          char c = name.charAt(1);

          if (c > 'a' && c < 'z')
          {
        	  c = (char) (c + 'A' - 'a');
          };
          sb.append(name.charAt(0)).append(c);
          sb.append(name.subSequence(2, name.length() - 2));
          char c1 = name.charAt(name.length() - 3);
          char c2 = name.charAt(name.length() - 2);
          if (c1 == 'I'  && c2 == 'd')
          {
        	  c2 = 'D';
          }
          sb.append(c2);
          sb.append(name.charAt(name.length() - 1));
          name = sb.toString();
          
          String value = objectTojson(props[i].getReadMethod().invoke(bean));  
          json.append(name);  
          json.append(":");  
          json.append(value);  
          json.append(",");  
        } catch (Exception e) {}  
      }  
      json.setCharAt(json.length() - 1, '}');  
    } else {  
      json.append("}");  
    }  
    return json.toString();  
  }  

  
  public static String listTojson(List<?> list) {  
    StringBuilder json = new StringBuilder();  
    json.append("[");  
    if (list != null && list.size() > 0) {  
      for (Object obj : list) {  
        json.append(objectTojson(obj));  
        json.append(",");  
      }  
      json.setCharAt(json.length() - 1, ']');  
    } else {  
      json.append("]");  
    }  
    return json.toString();  
  }  

   
  public static String arrayTojson(Object[] array) {  
    StringBuilder json = new StringBuilder();  
    json.append("[");  
    if (array != null && array.length > 0) {  
      for (Object obj : array) {  
        json.append(objectTojson(obj));  
        json.append(",");  
      }  
      json.setCharAt(json.length() - 1, ']');  
    } else {  
      json.append("]");  
    }  
    return json.toString();  
  }  

   
  public static String mapTojson(Map<?, ?> map) {  
    StringBuilder json = new StringBuilder();  
    json.append("{");  
    if (map != null && map.size() > 0) {  
      for (Object key : map.keySet()) {  
        json.append(objectTojson(key));  
        json.append(":");  
        json.append(objectTojson(map.get(key)));  
        json.append(",");  
      }  
      json.setCharAt(json.length() - 1, '}');  
    } else {  
      json.append("}");  
    }  
    return json.toString();  
  }  

   
  public static String setTojson(Set<?> set) {  
    StringBuilder json = new StringBuilder();  
    json.append("[");  
    if (set != null && set.size() > 0) {  
      for (Object obj : set) {  
        json.append(objectTojson(obj));  
        json.append(",");  
      }  
      json.setCharAt(json.length() - 1, ']');  
    } else {  
      json.append("]");  
    }  
    return json.toString();  
  }  

   
  public static String stringTojson(String s) {
    if (s == null)  
      return "";  
    StringBuilder sb = new StringBuilder();  
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);  
      switch (ch) {  
      case '"':  
        sb.append("\\\"");  
        break;  
      case '\\':  
        sb.append("\\\\");  
        break;  
      case '\b':  
        sb.append("\\b");  
        break;  
      case '\f':  
        sb.append("\\f");  
        break;  
      case '\n':  
        sb.append("\\n");  
        break;  
      case '\r':  
        sb.append("\\r");  
        break;  
      case '\t':  
        sb.append("\\t");  
        break;  
      case '/':  
        sb.append("\\/");  
        break;  
      default:  
        if (ch >= '\u0000' && ch <= '\u001F') {  
          String ss = Integer.toHexString(ch);  
          sb.append("\\u");  
          for (int k = 0; k < 4 - ss.length(); k++) {  
            sb.append('0');  
          }  
          sb.append(ss.toUpperCase());  
        } else {  
          sb.append(ch);  
        }  
      }  
    }  
    return sb.toString();  
  }  
}

