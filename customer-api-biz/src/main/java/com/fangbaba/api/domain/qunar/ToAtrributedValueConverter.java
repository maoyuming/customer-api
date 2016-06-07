package com.fangbaba.api.domain.qunar;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class ToAtrributedValueConverter implements Converter {

	@Override  
    public void marshal(Object obj, HierarchicalStreamWriter writer,MarshallingContext arg2) {  
    }  
  
    @Override  
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext arg1) {  
        City city = new City();  
        city.setCode(reader.getAttribute("code"));
        city.setCountryName(reader.getAttribute("countryName"));
        city.setCountryPy(reader.getAttribute("countryPy"));
        city.setName(reader.getAttribute("name"));
        city.setProvinceName(reader.getAttribute("provinceName"));
        city.setProvincePy(reader.getAttribute("provincePy"));
        return city;  
    }  
  
    @SuppressWarnings("unchecked")  
    @Override  
    public boolean canConvert(Class clazz) {  
        return clazz.equals(City.class);  
    } 
	
}  