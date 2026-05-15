package com.cts.mfrp.mediconnect.api.tests;

import com.cts.mfrp.mediconnect.api.base.ApiBaseTest;
import com.cts.mfrp.mediconnect.api.endpoints.InventoryEndpoints;
import com.cts.mfrp.mediconnect.api.models.Inventory;
import com.cts.mfrp.mediconnect.api.models.refs.HospitalRef;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class InventoryApiTest extends ApiBaseTest {

    private static final long SEED_HOSPITAL_ID = 1L;

    private InventoryEndpoints inventory;
    private Long createdId;

    @BeforeClass
    public void initEndpoints() {
        inventory = new InventoryEndpoints(spec);
    }

    @Test(priority = 1)
    public void getAll_shouldReturn200() {
        inventory.getAll().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 2)
    public void getByHospital_shouldReturnList() {
        inventory.getByHospital(SEED_HOSPITAL_ID).then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test(priority = 3)
    public void create_newItem_shouldSucceed() {
        Inventory body = new Inventory()
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setItemName("N95 Masks (Box)")
                .setCategory("Consumables")
                .setQuantity(200)
                .setReorderLevel(50);

        Response response = inventory.create(body);

        response.then()
                .statusCode(createdStatus())
                .body("itemId", notNullValue())
                .body("itemName", equalTo("N95 Masks (Box)"))
                .body("quantity", equalTo(200));

        createdId = response.jsonPath().getLong("itemId");
    }

    @Test(priority = 4, dependsOnMethods = "create_newItem_shouldSucceed")
    public void update_restock_shouldReflectQuantity() {
        Inventory body = new Inventory()
                .setHospital(new HospitalRef(SEED_HOSPITAL_ID))
                .setItemName("Surgical Gloves (Box)")
                .setCategory("Consumables")
                .setQuantity(800)
                .setReorderLevel(100);

        inventory.update(createdId, body).then()
                .statusCode(200)
                .body("itemName", equalTo("Surgical Gloves (Box)"))
                .body("quantity", equalTo(800));
    }

    @Test(priority = 5, dependsOnMethods = "create_newItem_shouldSucceed")
    public void delete_shouldReturnNoContent() {
        inventory.delete(createdId).then()
                .statusCode(deletedStatus());
    }
}
