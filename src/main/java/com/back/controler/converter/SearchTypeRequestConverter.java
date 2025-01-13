package com.back.controler.converter;


import com.back.domain.constant.SearchType;
import org.springframework.core.convert.converter.Converter;

public class SearchTypeRequestConverter implements Converter<String, SearchType> {
    @Override
    public SearchType convert(String searchTypeStr) {
        return SearchType.of(searchTypeStr);
    }
}
