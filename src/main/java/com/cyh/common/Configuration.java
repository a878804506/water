package com.cyh.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 解析配置文件
 */
public class Configuration{
	public static Map<String,String> propMap = new LinkedHashMap<String,String>();
	static{
		InputStream inputStream = null;
		try{
			inputStream = Configuration.class.getClassLoader().getResourceAsStream("application-prod.properties");
			Properties prop = new Properties();
			prop.load(inputStream);
			Iterator<Entry<Object,Object>> it = prop.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object,Object> entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				propMap.put(key.toString(),value.toString());
			}
		}catch(FileNotFoundException e){
			System.out.println("文件没有找到！");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
