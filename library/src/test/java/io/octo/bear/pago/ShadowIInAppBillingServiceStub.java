package io.octo.bear.pago;

import android.content.IntentSender;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import io.octo.bear.pago.model.entity.PurchaseType;

import static io.octo.bear.pago.MockUtils.TEST_DEVELOPER_PAYLOAD;
import static io.octo.bear.pago.MockUtils.TEST_PURCHASE_TOKEN;
import static io.octo.bear.pago.MockUtils.TEST_SKU;
import static io.octo.bear.pago.MockUtils.createBuyIntentBundle;
import static io.octo.bear.pago.MockUtils.createInventory;
import static io.octo.bear.pago.MockUtils.createPurchasedListBundle;
import static io.octo.bear.pago.MockUtils.createSkusInfoRequestBundle;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

/**
 * Created by shc on 21.07.16.
 */
@Implements(IInAppBillingService.Stub.class)
public class ShadowIInAppBillingServiceStub {

    @SuppressWarnings("unused")
    @Implementation
    public static com.android.vending.billing.IInAppBillingService asInterface(android.os.IBinder obj) throws Exception {
        final IInAppBillingService service = Mockito.mock(IInAppBillingService.class);

        setupBillingSupportedResponse(service, PurchaseType.INAPP);
        setupBillingSupportedResponse(service, PurchaseType.SUBSCRIPTION);

        setupDetailsResponse(service, PurchaseType.INAPP);
        setupDetailsResponse(service, PurchaseType.SUBSCRIPTION);

        setupBuyIntentResponse(service, PurchaseType.INAPP);
        setupBuyIntentResponse(service, PurchaseType.SUBSCRIPTION);

        setupConsumptionResponse(service);

        setupPurchasedItemsResponse(service, PurchaseType.INAPP);
        setupPurchasedItemsResponse(service, PurchaseType.SUBSCRIPTION);

        return service;
    }

    private static void setupPurchasedItemsResponse(IInAppBillingService service, PurchaseType type)
            throws RemoteException, IntentSender.SendIntentException {

        Mockito.doReturn(createPurchasedListBundle(type))
                .when(service)
                .getPurchases(
                        eq(Pago.BILLING_API_VERSION),
                        eq(PagoTest.PACKAGE_NAME),
                        eq(type.value),
                        anyString());
    }

    private static void setupConsumptionResponse(IInAppBillingService service) throws RemoteException {
        Mockito.doReturn(0)
                .when(service)
                .consumePurchase(
                        eq(Pago.BILLING_API_VERSION),
                        eq(PagoTest.PACKAGE_NAME),
                        eq(TEST_PURCHASE_TOKEN));

    }

    private static void setupBillingSupportedResponse(IInAppBillingService service, PurchaseType type) throws RemoteException {
        Mockito.doReturn(0)
                .when(service)
                .isBillingSupported(
                        eq(Pago.BILLING_API_VERSION),
                        eq(PagoTest.PACKAGE_NAME),
                        eq(type.value));
    }

    private static void setupDetailsResponse(IInAppBillingService service, PurchaseType type)
            throws RemoteException, IntentSender.SendIntentException {

        Mockito.doReturn(createInventory(type))
                .when(service)
                .getSkuDetails(
                        eq(Pago.BILLING_API_VERSION),
                        eq(PagoTest.PACKAGE_NAME),
                        eq(type.value),
                        argThat(new BundleMatcher(createSkusInfoRequestBundle(TEST_SKU))));

    }

    private static void setupBuyIntentResponse(IInAppBillingService service, PurchaseType type) throws RemoteException, IntentSender.SendIntentException {
        Mockito.doReturn(createBuyIntentBundle())
                .when(service)
                .getBuyIntent(
                        eq(Pago.BILLING_API_VERSION),
                        eq(PagoTest.PACKAGE_NAME),
                        eq(TEST_SKU),
                        eq(type.value),
                        eq(TEST_DEVELOPER_PAYLOAD));

    }

}