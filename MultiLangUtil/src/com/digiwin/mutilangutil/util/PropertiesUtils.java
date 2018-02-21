package com.digiwin.mutilangutil.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PropertiesUtils {
	private static final Logger log = LogManager.getLogger(PropertiesUtils.class);
	private static Properties props;
	public static String getProperty(String propertiesPath, String key) {	
		String value = "";
		props = new Properties();
		try {
//			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//			log.debug("this method caller,"+stackTraceElements[stackTraceElements.length-1].getClassName()+".");
//			log.debug(System.getProperty("user.dir"));			
			props.load(new FileInputStream(propertiesPath));
			value = props.getProperty(key);
		}catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			try {
				props.load(ClassLoader.class.getResourceAsStream(propertiesPath));
				value = props.getProperty(key);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("PropertiesUtils.getProperty()",e);
		}
        if(StringUtils.isBlank(value)){  
            return null;  
        }
        log.debug("Form Properites: " + propertiesPath+" ,key: "+key+" ,value: "+value);
		return value.trim();
	}	
	//url解碼成encode(ex:"UTF-8")
	public static String getProperty(String propertiesPath, String key, String encode) {	
		String value = "";		
		try {
			propertiesPath = URLDecoder.decode(propertiesPath, encode);
			value = getProperty(propertiesPath,key);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("PropertiesUtils.getProperty()",e);
		}
        if(StringUtils.isBlank(value)){  
            return null;  
        }
		return value.trim();
	}	
}
