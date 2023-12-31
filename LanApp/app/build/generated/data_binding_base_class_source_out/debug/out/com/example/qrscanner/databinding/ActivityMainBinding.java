// Generated by view binder compiler. Do not edit!
package com.example.qrscanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.qrscanner.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView balanceText2;

  @NonNull
  public final Button btnAuthenticate;

  @NonNull
  public final Button btnSend;

  @NonNull
  public final Button btnTransactionHistory;

  @NonNull
  public final ConstraintLayout mainLayout;

  @NonNull
  public final TabLayout tabLayout;

  @NonNull
  public final ViewPager2 viewPager;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull TextView balanceText2,
      @NonNull Button btnAuthenticate, @NonNull Button btnSend,
      @NonNull Button btnTransactionHistory, @NonNull ConstraintLayout mainLayout,
      @NonNull TabLayout tabLayout, @NonNull ViewPager2 viewPager) {
    this.rootView = rootView;
    this.balanceText2 = balanceText2;
    this.btnAuthenticate = btnAuthenticate;
    this.btnSend = btnSend;
    this.btnTransactionHistory = btnTransactionHistory;
    this.mainLayout = mainLayout;
    this.tabLayout = tabLayout;
    this.viewPager = viewPager;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.balanceText2;
      TextView balanceText2 = ViewBindings.findChildViewById(rootView, id);
      if (balanceText2 == null) {
        break missingId;
      }

      id = R.id.btn_authenticate;
      Button btnAuthenticate = ViewBindings.findChildViewById(rootView, id);
      if (btnAuthenticate == null) {
        break missingId;
      }

      id = R.id.btn_send;
      Button btnSend = ViewBindings.findChildViewById(rootView, id);
      if (btnSend == null) {
        break missingId;
      }

      id = R.id.btn_transactionHistory;
      Button btnTransactionHistory = ViewBindings.findChildViewById(rootView, id);
      if (btnTransactionHistory == null) {
        break missingId;
      }

      ConstraintLayout mainLayout = (ConstraintLayout) rootView;

      id = R.id.tabLayout;
      TabLayout tabLayout = ViewBindings.findChildViewById(rootView, id);
      if (tabLayout == null) {
        break missingId;
      }

      id = R.id.viewPager;
      ViewPager2 viewPager = ViewBindings.findChildViewById(rootView, id);
      if (viewPager == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, balanceText2, btnAuthenticate,
          btnSend, btnTransactionHistory, mainLayout, tabLayout, viewPager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
