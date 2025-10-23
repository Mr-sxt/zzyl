package com.zzyl;

import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsRequest;
import com.huaweicloud.sdk.iotda.v5.model.ListProductsResponse;
import com.huaweicloud.sdk.iotda.v5.model.ProductSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class IoTDeviceTest {

    @Autowired
    private IoTDAClient iotDAClient;

    @Test
    public void testGetProdectList() {
        ListProductsRequest request = new ListProductsRequest();
        request.setLimit(50);
        ListProductsResponse listProductsResponse = iotDAClient.listProducts(request);
        List<ProductSummary> products = listProductsResponse.getProducts();
        System.out.println(products);
    }
}
