package com.cyh.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 解析配置文件
 */
public class UploadConfigurer extends PropertyPlaceholderConfigurer{

	public static Map<String,String> propMap = new LinkedHashMap<>();

	private Resource[] locations;

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void setLocalOverride(boolean localOverride) {
		this.localOverride = localOverride;
	}

	/**
	 * 根据启动参数,动态读取配置文件
	 */
	@Override
	protected void loadProperties(Properties props) throws IOException {
		if (locations != null) {
			// 使用的配置文件  是 application-dev.properties  还是 application-prod.properties
			String configFileName = null;
			for (Resource location : this.locations) { // 此循环找到 application.properties
				InputStream inputStream = null;
				try {
					String filename = location.getFilename();
					if("application.properties".equals(filename)){
						inputStream = location.getInputStream();
						Properties tempProp = new Properties();
						tempProp.load(inputStream);
						Iterator<Map.Entry<Object,Object>> it = tempProp.entrySet().iterator();
						while(it.hasNext()){
							Map.Entry<Object,Object> entry = it.next();
							Object key = entry.getKey();
							if("system.useingConfig".equals(key)){
								configFileName = entry.getValue().toString();
								break;
							}
						}
						break;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
			if(StringUtils.isNotEmpty(configFileName)){
				for (Resource location : this.locations) {  // 此循环根据application.properties中的文件名字  加载指定配置文件
					InputStream inputStream = null;
					try {
						String filename = location.getFilename();
						if(configFileName.equals(filename)){
							System.out.println("=====================加载配置文件为："+configFileName+"=====================");
							inputStream = location.getInputStream();
							/**
							 * 这里是坑  一定要将读取的配置文件 放到入参的这个props里面
							 */
							props.load(inputStream);
							Iterator<Map.Entry<Object,Object>> it = props.entrySet().iterator();
							while(it.hasNext()){
								Map.Entry<Object,Object> entry = it.next();
								Object key = entry.getKey();
								Object value = entry.getValue();
								propMap.put(key.toString(),value.toString());
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
					}
				}
			}
		}
	}

	/**
	 * 重写父类方法，解密指定属性名对应的属性值
	 */
	@Override
	protected String convertProperty(String propertyName,String propertyValue){
		if(isEncryptPropertyVal(propertyName)){
			try{
				return com.alibaba.druid.filter.config.ConfigTools.decrypt(propMap.get("jdbc.passwordPublicKey"),propertyValue);
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}else{
			return propertyValue;
		}
	}

	/**
	 * 判断属性值是否需要解密，这里我约定需要解密的属性名用encrypt开头
	 */
	private boolean isEncryptPropertyVal(String propertyName){
		if(propertyName.startsWith("jdbc.encrypt_")){
			return true;
		}else{
			return false;
		}
	}
}
