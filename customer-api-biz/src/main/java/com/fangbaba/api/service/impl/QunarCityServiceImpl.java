package com.fangbaba.api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangbaba.api.domain.qunar.City;
import com.fangbaba.api.domain.qunar.ToAtrributedValueConverter;
import com.fangbaba.api.service.CacheService;
import com.fangbaba.api.service.QunarCityService;
import com.fangbaba.api.util.Config;
import com.fangbaba.basic.face.bean.CityModel;
import com.fangbaba.basic.face.service.CityService;
import com.fangbaba.gds.enums.ChannelEnum;
import com.fangbaba.gds.face.bean.CityMapping;
import com.fangbaba.gds.face.service.ICityMappingService;
import com.fangbaba.gds.face.service.IDistributorConfigService;
import com.fangbaba.gds.po.DistributorConfig;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

@Service
public class QunarCityServiceImpl implements QunarCityService{
	

	private static Logger logger = LoggerFactory.getLogger(QunarCityServiceImpl.class);

	@Autowired
	private CityService cityService;
	@Autowired
	private ICityMappingService iCityMappingService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private IDistributorConfigService iDistributorConfigService;

	
    public void initCity() throws FileNotFoundException{

		logger.info("开始初始化城市");
		//查询xml
		File file = new File(Config.getValue("qunar.citymapping"));
		
		XStream xstream = new XStream();  
		xstream.alias("list", List.class);
        xstream.alias("city", City.class);  

        xstream.registerConverter(new ToAtrributedValueConverter());
//		CityInfo list =(CityInfo)xstream.fromXML(file);  
//        String xml =  xstream.toXML(file);
		
        Object obj = xstream.fromXML(new FileInputStream(file));
        //三种方法都可
        @SuppressWarnings("unchecked")
//        CityInfo cityInfo = (CityInfo)xstream.fromXML(new FileInputStream(file));
//        @SuppressWarnings("unchecked")
		List<City> list = (List<City>)xstream.fromXML(new FileInputStream(file));
        
        List<CityModel> cityList = cityService.queryAllCitys();
        
        Map<String, String> map = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(cityList)){
        	for (CityModel cityModel : cityList) {
        		map.put(cityModel.getQuerycityname().substring(0,cityModel.getQuerycityname().length()-1), cityModel.getCode());
        		map.put(cityModel.getQuerycityname().substring(0,cityModel.getQuerycityname().length()), cityModel.getCode());
			}
        }
        
        //根据channelid查询otatype
        Long otatype = null;
		List<DistributorConfig>  distributorConfigs =iDistributorConfigService.queryByChannelId(ChannelEnum.QUNAR.getId());
		if(CollectionUtils.isNotEmpty(distributorConfigs)){
			otatype = distributorConfigs.get(0).getOtatype();
			
		}
        if(CollectionUtils.isNotEmpty(list)){
        	int count=0;
        	for (City city : list) {
				if(map.containsKey(city.getName())){
					CityMapping cityMapping = new CityMapping();
					cityMapping.setCode(map.get(city.getName()));
					cityMapping.setOtatype(otatype);
					cityMapping.setOtacode(city.getCode());
					cityMapping.setChannelid(ChannelEnum.QUNAR.getId());
					iCityMappingService.saveOrUpdate(cityMapping);
					count++;
				}
			}
        	
        	logger.info("一共保存了{}条记录",count);
        }
        
        cacheService.initArea();
        
//		InputStream in = null;
//        try {
//            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
//            // 一次读一个字节
//            in = new FileInputStream(file);
//            int tempbyte;
//            while ((tempbyte = in.read()) != -1) {
//                System.out.write(tempbyte);
////                City city =(City)xstream.fromXML(tempbyte);  
//            }
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
        logger.info(new Gson().toJson(list));
//        logger.info(new Gson().toJson(cityInfo));
	
    }
}
