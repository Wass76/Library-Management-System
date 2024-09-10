package com.BaseProject.utils.service;

import com.BaseProject.utils.request.BaseRequest;
import com.BaseProject.utils.response.ApiResponseClass;
import org.springframework.stereotype.Service;

@Service
public interface BaseService {

    public ApiResponseClass getAllItems();
    public ApiResponseClass getItemById(int id);
    public ApiResponseClass saveItem(BaseRequest request);
    public ApiResponseClass updateItem(BaseRequest request , int id);
    public ApiResponseClass deleteItem(int id);
}
