package com.pika.gstore.search.service;

import com.pika.gstore.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author pi'ka'chu
 */
public interface ProductSaveService {
    boolean saveEs(List<SkuEsModel> skuEsModels) throws IOException;
}
