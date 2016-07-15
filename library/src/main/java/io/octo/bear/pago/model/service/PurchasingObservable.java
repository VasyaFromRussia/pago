package io.octo.bear.pago.model.service;

import android.content.Context;
import android.os.RemoteException;

import io.octo.bear.pago.model.entity.Purchase;
import io.octo.bear.pago.model.entity.PurchaseType;
import rx.Single;

/**
 * Created by shc on 14.07.16.
 */

public class PurchasingObservable extends Single<Purchase> {

    public PurchasingObservable(final Context context, final PurchaseType type, final String sku) {
        super(subscriber -> {
            try {
                final BillingServiceHelper billingServiceHelper = new BillingServiceHelper();
                billingServiceHelper.purchaseItem(context, sku, type, subscriber::onSuccess);
            } catch (RemoteException e) {
                subscriber.onError(e);
            }
        });
    }

}
