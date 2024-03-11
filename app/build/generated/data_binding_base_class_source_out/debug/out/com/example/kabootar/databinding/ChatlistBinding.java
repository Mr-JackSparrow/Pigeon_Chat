// Generated by view binder compiler. Do not edit!
package com.example.kabootar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kabootar.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ChatlistBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ShapeableImageView ivCallProfileImg;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final ConstraintLayout mainChat;

  @NonNull
  public final TextView tvLastMsg;

  @NonNull
  public final TextView tvLastMsgTime;

  @NonNull
  public final TextView tvProfileName;

  private ChatlistBinding(@NonNull ConstraintLayout rootView,
      @NonNull ShapeableImageView ivCallProfileImg, @NonNull LinearLayout linearLayout,
      @NonNull ConstraintLayout mainChat, @NonNull TextView tvLastMsg,
      @NonNull TextView tvLastMsgTime, @NonNull TextView tvProfileName) {
    this.rootView = rootView;
    this.ivCallProfileImg = ivCallProfileImg;
    this.linearLayout = linearLayout;
    this.mainChat = mainChat;
    this.tvLastMsg = tvLastMsg;
    this.tvLastMsgTime = tvLastMsgTime;
    this.tvProfileName = tvProfileName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ChatlistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ChatlistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.chatlist, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ChatlistBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivCallProfileImg;
      ShapeableImageView ivCallProfileImg = ViewBindings.findChildViewById(rootView, id);
      if (ivCallProfileImg == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      ConstraintLayout mainChat = (ConstraintLayout) rootView;

      id = R.id.tvLastMsg;
      TextView tvLastMsg = ViewBindings.findChildViewById(rootView, id);
      if (tvLastMsg == null) {
        break missingId;
      }

      id = R.id.tvLastMsgTime;
      TextView tvLastMsgTime = ViewBindings.findChildViewById(rootView, id);
      if (tvLastMsgTime == null) {
        break missingId;
      }

      id = R.id.tvProfileName;
      TextView tvProfileName = ViewBindings.findChildViewById(rootView, id);
      if (tvProfileName == null) {
        break missingId;
      }

      return new ChatlistBinding((ConstraintLayout) rootView, ivCallProfileImg, linearLayout,
          mainChat, tvLastMsg, tvLastMsgTime, tvProfileName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
