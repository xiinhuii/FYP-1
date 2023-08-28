// Generated by view binder compiler. Do not edit!
package com.example.qrscanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qrscanner.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentTransactionBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Toolbar homeToolbar;

  @NonNull
  public final ListView transactionListView;

  private FragmentTransactionBinding(@NonNull LinearLayout rootView, @NonNull Toolbar homeToolbar,
      @NonNull ListView transactionListView) {
    this.rootView = rootView;
    this.homeToolbar = homeToolbar;
    this.transactionListView = transactionListView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentTransactionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentTransactionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_transaction, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentTransactionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.homeToolbar;
      Toolbar homeToolbar = ViewBindings.findChildViewById(rootView, id);
      if (homeToolbar == null) {
        break missingId;
      }

      id = R.id.transactionListView;
      ListView transactionListView = ViewBindings.findChildViewById(rootView, id);
      if (transactionListView == null) {
        break missingId;
      }

      return new FragmentTransactionBinding((LinearLayout) rootView, homeToolbar,
          transactionListView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
